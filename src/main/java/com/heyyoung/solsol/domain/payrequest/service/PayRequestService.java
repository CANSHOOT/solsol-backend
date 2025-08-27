package com.heyyoung.solsol.domain.payrequest.service;

import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayGroup;
import com.heyyoung.solsol.domain.dutchpay.repository.DutchPayGroupRepository;
import com.heyyoung.solsol.domain.payrequest.dto.CreatePayRequest;
import com.heyyoung.solsol.domain.payrequest.dto.PayParticipantCommand;
import com.heyyoung.solsol.domain.payrequest.dto.PayRequestCreatedResponse;
import com.heyyoung.solsol.domain.payrequest.dto.PayRequestDetailResponse;
import com.heyyoung.solsol.domain.payrequest.dto.PayRequestSummary;
import com.heyyoung.solsol.domain.payrequest.entity.PayParticipantStatus;
import com.heyyoung.solsol.domain.payrequest.entity.PayRequest;
import com.heyyoung.solsol.domain.payrequest.entity.PayRequestParticipant;
import com.heyyoung.solsol.domain.payrequest.repository.PayRequestParticipantRepository;
import com.heyyoung.solsol.domain.payrequest.repository.PayRequestRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayRequestService {

    private final PayRequestRepository payRequestRepository;
    private final PayRequestParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final DutchPayGroupRepository groupRepository;

    /* -------------------------
     * 생성
     * ------------------------- */
    @Transactional
    public PayRequestCreatedResponse create(CreatePayRequest req) {
        User requester = userRepository.findByUserId(req.getRequesterId())
                .orElseThrow(() -> new IllegalArgumentException("requester not found: " + req.getRequesterId()));

        DutchPayGroup group = null;
        if (req.getGroupId() != null) {
            group = groupRepository.findById(req.getGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("group not found: " + req.getGroupId()));
        }

        BigDecimal total = req.getParticipants().stream()
                .map(CreatePayRequest.Item::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PayRequest payRequest = PayRequest.builder()
                .requester(requester)
                .dutchPayGroup(group)
                .memo(req.getMemo())
                .totalAmount(total)
                .build();

        // 참가자 생성
        for (CreatePayRequest.Item item : req.getParticipants()) {
            User payer = userRepository.findByUserId(item.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("payer not found: " + item.getUserId()));

            PayRequestParticipant p = PayRequestParticipant.builder()
                    .payRequest(payRequest)
                    .user(payer)
                    .amount(item.getAmount())
                    .status(PayParticipantStatus.REQUESTED)
                    .build();

            payRequest.addParticipant(p);
        }

        PayRequest saved = payRequestRepository.save(payRequest);

        // FCM 푸시는 Step 3에서: 각 참가자에게 “정산 요청이 도착했어요” 알림

        return new PayRequestCreatedResponse(saved.getPayRequestId());
    }

    /* -------------------------
     * 조회 - 내가 보낸 요청 묶음
     * ------------------------- */
    @Transactional(readOnly = true)
    public List<PayRequestSummary> listSent(Long requesterId) {
        return payRequestRepository.findByRequester_UserIdOrderByCreatedAtDesc(requesterId).stream()
                .map(r -> new PayRequestSummary(
                        r.getPayRequestId(),
                        r.getRequester().getUserId(),
                        r.getDutchPayGroup() != null ? r.getDutchPayGroup().getGroupId() : null,
                        r.getMemo(),
                        r.getTotalAmount(),
                        r.getCreatedAt()
                ))
                .collect(toList());
    }

    /* -------------------------
     * 조회 - 내가 받은 요청 묶음
     * ------------------------- */
    @Transactional(readOnly = true)
    public List<PayRequestSummary> listReceived(Long userId) {
        // 내가 참가자인 payRequest들만 골라서 헤더 요약 반환
        return participantRepository.findByUser_UserIdOrderByCreatedAtDesc(userId).stream()
                .map(PayRequestParticipant::getPayRequest)
                .distinct()
                .map(r -> new PayRequestSummary(
                        r.getPayRequestId(),
                        r.getRequester().getUserId(),
                        r.getDutchPayGroup() != null ? r.getDutchPayGroup().getGroupId() : null,
                        r.getMemo(),
                        r.getTotalAmount(),
                        r.getCreatedAt()
                ))
                .collect(toList());
    }

    /* -------------------------
     * 상세 조회 (참가자 목록 포함)
     * ------------------------- */
    @Transactional(readOnly = true)
    public PayRequestDetailResponse getDetail(Long payRequestId) {
        PayRequest r = payRequestRepository.findById(payRequestId)
                .orElseThrow(() -> new IllegalArgumentException("payRequest not found: " + payRequestId));

        var rows = participantRepository.findByPayRequest_PayRequestId(payRequestId).stream()
                .map(p -> new PayRequestDetailResponse.Row(
                        p.getPayRequestParticipantId(),
                        p.getUser().getUserId(),
                        p.getAmount(),
                        p.getStatus(),
                        p.getTransferUniqueNo()
                ))
                .collect(toList());

        return new PayRequestDetailResponse(
                r.getPayRequestId(),
                r.getRequester().getUserId(),
                r.getDutchPayGroup() != null ? r.getDutchPayGroup().getGroupId() : null,
                r.getMemo(),
                r.getTotalAmount(),
                r.getCreatedAt(),
                rows
        );
    }

    /* -------------------------
     * 결제 (다음 Step에서 실제 송금 + 상태변경 + FCM 붙임)
     * ------------------------- */
    @Transactional
    public void pay(PayParticipantCommand.PayRequest cmd) {
        // Step 3에서 구현:
        // 1) participant 조회/검증(REQUESTED & userId 일치)
        // 2) AccountApiService 통해 실제 송금 (B/C → A)
        // 3) 성공 시 markPaid(transferUniqueNo)
        // 4) FCM: 요청자/결제자 모두에게 알림
        log.info("[STUB] pay() called participantId={}, payerUserId={}", cmd.getParticipantId(), cmd.getPayerUserId());
    }
}
