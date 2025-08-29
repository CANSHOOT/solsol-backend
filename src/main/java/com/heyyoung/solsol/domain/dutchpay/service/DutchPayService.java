package com.heyyoung.solsol.domain.dutchpay.service;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.dutchpay.dto.*;
import com.heyyoung.solsol.domain.dutchpay.entity.*;
import com.heyyoung.solsol.domain.dutchpay.repository.DutchPayGroupRepository;
import com.heyyoung.solsol.domain.dutchpay.repository.DutchPayParticipantRepository;
import com.heyyoung.solsol.domain.notification.service.FCMService;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import com.heyyoung.solsol.external.dto.account.AccountBalanceResponse;
import com.heyyoung.solsol.external.dto.account.AccountTransferResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 더치페이 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DutchPayService {

    private final DutchPayGroupRepository dutchPayGroupRepository;
    private final DutchPayParticipantRepository dutchPayParticipantRepository;
    private final UserRepository userRepository;
    private final AccountApiService accountApiService;
    private final FCMService fcmService;

    /**
     * 더치페이 생성
     * @param request 더치페이 생성 요청
     * @param userId 주최자 ID
     * @return 생성된 더치페이 정보
     */
    @Transactional
    public DutchPayResponse createDutchPay(CreateDutchPayRequest request, String userId) {
        User organizer = findUserById(userId);

        // 더치페이 그룹 생성
        DutchPayGroup dutchPayGroup = DutchPayGroup.builder()
                .organizer(organizer)
                .paymentId(request.getPaymentId())
                .groupName(request.getGroupName())
                .totalAmount(request.getTotalAmount())
                .participantCount(request.getParticipantCount())
                .build();

        DutchPayGroup savedGroup = dutchPayGroupRepository.save(dutchPayGroup);
        log.info("더치페이 그룹 생성 완료 - GroupId: {}, Organizer: {}", savedGroup.getGroupId(), userId);

        // 기본 응답 생성
        DutchPayResponse response = DutchPayResponse.from(savedGroup);

        // 초대할 사용자 목록 결정
        List<String> inviteUserIds = request.getInviteUserIds();
        if (inviteUserIds == null || inviteUserIds.isEmpty()) {
            // inviteUserIds가 없으면 participantUserIds 사용 (새로 추가)
            inviteUserIds = request.getParticipantUserIds();
        }

        // 초대할 사용자 목록이 있다면 자동으로 푸시 알림 전송
        if (inviteUserIds != null && !inviteUserIds.isEmpty()) {
            try {
                // 트랜잭션 커밋 후 푸시 알림 전송 (현재는 동기 처리)
                inviteUsersToDutchPay(savedGroup.getGroupId(), inviteUserIds, userId);
                log.info("더치페이 생성 시 자동 초대 완료 - GroupId: {}, 초대 사용자 수: {}",
                        savedGroup.getGroupId(), inviteUserIds.size());
            } catch (Exception e) {
                log.error("더치페이 생성 시 자동 초대 실패 - GroupId: {}, Error: {}",
                        savedGroup.getGroupId(), e.getMessage());
                // 푸시 알림 실패해도 더치페이 생성은 성공으로 처리
            }
        }

        return response;
    }

    /**
     * 더치페이에 특정 사용자들을 초대하고 푸시 알림 전송
     * @param groupId 그룹 ID
     * @param inviteUserIds 초대할 사용자 ID 목록
     * @param organizerId 주최자 ID
     */
    @Transactional
    public void inviteUsersToDutchPay(Long groupId, List<String> inviteUserIds, String organizerId) {
        DutchPayGroup dutchPayGroup = findDutchPayGroupById(groupId);
        User organizer = findUserById(organizerId);

        log.info("더치페이 초대 시작 - GroupId: {}, 초대할 사용자 수: {}", groupId, inviteUserIds.size());

        for (String userId : inviteUserIds) {
            try {
                User invitedUser = findUserById(userId);

                // 사용자의 FCM 토큰이 있는 경우에만 푸시 알림 전송
                if (invitedUser.hasFcmToken()) {
                    fcmService.sendDutchPayInviteNotification(
                            invitedUser.getFcmToken(),
                            organizer.getName(),
                            dutchPayGroup.getGroupName(),
                            groupId,
                            organizerId
                    );
                    log.info("더치페이 초대 푸시 알림 전송 완료 - UserId: {}, GroupId: {}", userId, groupId);
                } else {
                    log.warn("FCM 토큰이 없는 사용자 - UserId: {}", userId);
                }
            } catch (Exception e) {
                log.error("더치페이 초대 푸시 알림 전송 실패 - UserId: {}, Error: {}", userId, e.getMessage());
                // 한 명의 알림 실패가 전체 프로세스를 중단시키지 않도록 continue
            }
        }

        log.info("더치페이 초대 프로세스 완료 - GroupId: {}", groupId);
    }

    /**
     * 더치페이 조회
     * @param groupId 그룹 ID
     * @return 더치페이 정보
     */
    @Transactional(readOnly = true)
    public DutchPayResponse getDutchPayById(Long groupId) {
        DutchPayGroup dutchPayGroup = findDutchPayGroupById(groupId);
        return DutchPayResponse.from(dutchPayGroup);
    }

    /**
     * 더치페이 참여
     * @param groupId 그룹 ID
     * @param request 참여 요청
     * @param userId 사용자 ID
     * @return 참여자 정보
     */
    @Transactional
    public ParticipantResponse joinDutchPay(Long groupId, JoinDutchPayRequest request, String userId) {
        DutchPayGroup dutchPayGroup = findDutchPayGroupById(groupId);
        User user = findUserById(userId);

        // 이미 참여한 사용자인지 확인
        if (dutchPayParticipantRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new SolsolException(SolsolErrorCode.ALREADY_PARTICIPATED);
        }

        // 더치페이 상태 확인
        if (dutchPayGroup.getStatus() != DutchPayStatus.ACTIVE) {
            throw new SolsolException(SolsolErrorCode.INVALID_DUTCH_PAY_STATUS);
        }

        // 참여자 생성
        DutchPayParticipant participant = DutchPayParticipant.builder()
                .dutchPayGroup(dutchPayGroup)
                .user(user)
                .joinMethod(request.getJoinMethod())
                .build();

        // 초기 정산 금액 0원 설정 (명시적으로)
        participant.updateSettlementAmount(request.getSettlementAmount());

        DutchPayParticipant savedParticipant = dutchPayParticipantRepository.save(participant);
        log.info("더치페이 참여 완료 - GroupId: {}, UserId: {}", groupId, userId);

        return ParticipantResponse.from(savedParticipant);
    }

    /**
     * 송금 처리 (금융 API 호출)
     * @param groupId 그룹 ID
     * @param request 송금 요청
     * @param userId 사용자 ID
     * @return 송금 처리 결과
     */
    @Transactional
    public PaymentResponse sendPayment(Long groupId, SendPaymentRequest request, String userId) {
        DutchPayGroup dutchPayGroup = findDutchPayGroupById(groupId);

        // 참여자 확인
        DutchPayParticipant participant = dutchPayParticipantRepository
                .findByGroupIdAndUserId(groupId, userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.PARTICIPANT_NOT_FOUND));

        // 이미 결제한 참여자인지 확인
        if (participant.getPaymentStatus() == ParticipantPaymentStatus.COMPLETED) {
            throw new SolsolException(SolsolErrorCode.ALREADY_PAID);
        }

        try {
            User participantUser = findUserById(userId);
            User organizerUser = dutchPayGroup.getOrganizer();

            // 참여자와 주최자의 계좌번호 확인
            if (participantUser.getAccountNo() == null || organizerUser.getAccountNo() == null) {
                throw new RuntimeException("계좌 정보가 없는 사용자입니다.");
            }

            // 금융 API를 통한 계좌 이체 (참여자 -> 주최자)
            AccountTransferResponse transferResponse = accountApiService.transferAccount(
                    participantUser.getUserKey(),
                    organizerUser.getAccountNo(), // 입금계좌 (주최자의 실제 계좌번호)
                    request.getTransactionSummary() != null ? request.getTransactionSummary() : "더치페이 정산",
                    participant.getSettlementAmount().toString(), // 참여자가 내야 할 돈
                    participantUser.getAccountNo(), // 출금계좌 (참여자의 실제 계좌번호)
                    "더치페이 송금"
            );

            // 거래 고유번호 추출 (첫 번째 거래 기록에서)
            String transactionId = transferResponse.getREC().get(0).getTransactionUniqueNo();

            // 참여자 결제 상태 업데이트
            participant.completePayment(transactionId);

            // 이체 후 참여자와 주최자의 잔액 업데이트
            updateUserBalance(participantUser);
            updateUserBalance(organizerUser);

            log.info("더치페이 송금 완료 - GroupId: {}, UserId: {}, TransactionId: {}",
                    groupId, userId, transactionId);

            // 모든 참여자가 결제 완료했는지 확인
            checkAndCompleteGroup(dutchPayGroup);

            return PaymentResponse.success(transactionId, participant.getSettlementAmount());

        } catch (Exception e) {
            log.error("더치페이 송금 실패 - GroupId: {}, UserId: {}, Error: {}",
                    groupId, userId, e.getMessage());

            // 참여자 결제 상태를 실패로 업데이트
            participant.failPayment();

            return PaymentResponse.failure("송금 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자별 더치페이 히스토리 조회
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 더치페이 히스토리
     */
    @Transactional(readOnly = true)
    public List<DutchPayResponse> getDutchPayHistory(String userId, Pageable pageable) {
        // 주최한 더치페이 목록
        List<DutchPayGroup> organizedGroups = dutchPayGroupRepository.findByOrganizerUserId(userId, pageable);

        return organizedGroups.stream()
                .map(DutchPayResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 참여 중인 더치페이 목록 조회
     * @param userId 사용자 ID
     * @return 참여자 목록
     */
    @Transactional(readOnly = true)
    public List<ParticipantResponse> getUserParticipations(String userId) {
        List<DutchPayParticipant> participants = dutchPayParticipantRepository.findByUserIdWithGroup(userId);

        return participants.stream()
                .map(ParticipantResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 엔티티
     */
    private User findUserById(String userId) {
        return userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));
    }

    /**
     * 더치페이 그룹 조회
     * @param groupId 그룹 ID
     * @return 더치페이 그룹 엔티티
     */
    private DutchPayGroup findDutchPayGroupById(Long groupId) {
        return dutchPayGroupRepository.findByIdWithParticipants(groupId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.DUTCH_PAY_NOT_FOUND));
    }

    /**
     * 사용자 잔액 업데이트
     * 금융 API를 통해 최신 잔액을 조회하여 DB에 반영
     * @param user 업데이트할 사용자
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateUserBalance(User user) {
        try {
            AccountBalanceResponse balanceResponse = accountApiService.inquireAccountBalance(
                    user.getUserKey(),
                    user.getAccountNo()
            );

            // 잔액을 Long 타입으로 변환 (String -> Long)
            Long newBalance = Long.parseLong(balanceResponse.getREC().getAccountBalance());
            user.updateAccountBalance(newBalance);

            log.info("사용자 잔액 업데이트 완료 - UserId: {}, AccountNo: {}, Balance: {}",
                    user.getUserId(), user.getAccountNo(), newBalance);

        } catch (Exception e) {
            log.error("사용자 잔액 업데이트 실패 - UserId: {}, Error: {}",
                    user.getUserId(), e.getMessage());
            // 잔액 업데이트 실패해도 이체는 완료되었으므로 예외를 던지지 않음
        }
    }

    /**
     * 모든 참여자 결제 완료 확인 및 그룹 상태 업데이트
     * @param dutchPayGroup 더치페이 그룹
     */
    private void checkAndCompleteGroup(DutchPayGroup dutchPayGroup) {
        Long completedCount = dutchPayParticipantRepository
                .countByGroupIdAndPaymentStatus(dutchPayGroup.getGroupId(), ParticipantPaymentStatus.COMPLETED);

        // 모든 참여자가 결제 완료했다면 그룹 상태를 완료로 변경
        if (completedCount >= dutchPayGroup.getParticipantCount()) {
            dutchPayGroup.updateStatus(DutchPayStatus.COMPLETED);
            log.info("더치페이 그룹 완료 - GroupId: {}", dutchPayGroup.getGroupId());
        }
    }

    /**
     * 내 정산 상태 조회
     * @param userId 사용자 ID
     */
    @Transactional
    public MySettlementSummaryResponse getMySettlementSummary(String userId) {

        // 1) 내가 보낼 돈 (내가 참가자인 항목들)
        List<DutchPayParticipant> myParticipations = dutchPayParticipantRepository.findByUser_UserId(userId);

        List<MyPayableItemResponse> payables = myParticipations.stream()
                .map(p -> {
                    var g = p.getDutchPayGroup();
                    String organizerUserId = resolveOrganizerUserId(g);
                    String organizerName   = resolveOrganizerName(g);
                    return new MyPayableItemResponse(
                            g.getGroupId(),
                            g.getGroupName(),
                            organizerUserId,
                            organizerName,
                            p.getSettlementAmount(),
                            toKoreanStatus(p.getPaymentStatus())
                    );
                })
                .sorted(Comparator.comparing(MyPayableItemResponse::groupId))
                .toList().reversed();

        // 2) 내가 받을 돈 (내가 organizer인 그룹들의 모든 참가자들)
        List<DutchPayGroup> myGroups = findGroupsByOrganizer(userId);
        List<MyReceivableItemResponse> receivables = new ArrayList<>();

        for (DutchPayGroup g : myGroups) {
            List<DutchPayParticipant> participants =
                    dutchPayParticipantRepository.findByDutchPayGroup_GroupId(g.getGroupId());

            for (DutchPayParticipant p : participants) {
                // 주최자 자신은 빼고 싶으면 주석 해제
                // if (userId.equals(p.getUser().getUserId())) continue;

                receivables.add(new MyReceivableItemResponse(
                        g.getGroupId(),
                        g.getGroupName(),
                        p.getUser().getUserId(),
                        p.getUser().getName(),
                        p.getSettlementAmount(),
                        toKoreanStatus(p.getPaymentStatus())
                ));
            }
        }

        receivables.sort(
                Comparator.comparing(MyReceivableItemResponse::groupId).reversed()
                        .thenComparing(MyReceivableItemResponse::userName)
        );

        return MySettlementSummaryResponse.of(payables, receivables);
    }

    private List<DutchPayGroup> findGroupsByOrganizer(String userId) {
        try {
            // 연관관계인 경우
            return dutchPayGroupRepository.findByOrganizer_UserId(userId);
        } catch (Exception ignore) {
            // organizerId(String) 필드인 경우
            return dutchPayGroupRepository.findByOrganizer_UserId(userId);
        }
    }

    private String resolveOrganizerUserId(DutchPayGroup g) {
        try {
            if (g.getOrganizer() != null) return g.getOrganizer().getUserId();
        } catch (NoSuchMethodError | NullPointerException ignored) {}
        return g.getOrganizer().getUserId(); // String 필드인 경우
    }

    private String resolveOrganizerName(DutchPayGroup g) {
        try {
            if (g.getOrganizer() != null) return g.getOrganizer().getName();
        } catch (NoSuchMethodError | NullPointerException ignored) {}

        // organizerId만 있는 경우 UserRepo로 이름 조회
        try {
            return userRepository.findById(g.getOrganizer().getUserId())
                    .map(User::getName)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    private String toKoreanStatus(ParticipantPaymentStatus status) {
        if (status == null) return "기타";
        return switch (status) {
            case PENDING   -> "진행중";
            case COMPLETED -> "완료";
            case FAILED    -> "실패";
        };
    }
}