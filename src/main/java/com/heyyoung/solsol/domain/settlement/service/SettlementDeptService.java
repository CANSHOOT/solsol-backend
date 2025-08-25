package com.heyyoung.solsol.domain.settlement.service;

import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureRow;
import com.heyyoung.solsol.domain.settlement.dto.CouncilFeeView;
import com.heyyoung.solsol.domain.settlement.dto.DeptExpenditureListResponse;
import com.heyyoung.solsol.domain.settlement.dto.DeptHomeSummaryResponse;
import com.heyyoung.solsol.domain.settlement.dto.StudentCouncilView;
import com.heyyoung.solsol.domain.settlement.repository.CouncilExpenditureRepository;
import com.heyyoung.solsol.domain.settlement.repository.CouncilFeePaymentRepository;
import com.heyyoung.solsol.domain.settlement.repository.CouncilFeeRepository;
import com.heyyoung.solsol.domain.settlement.repository.StudentCouncilRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import com.heyyoung.solsol.external.dto.account.AccountBalanceResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SettlementDeptService {

    private final StudentCouncilRepository councilRepo;
    private final CouncilExpenditureRepository expRepo;
    private final CouncilFeeRepository feeRepo;
    private final CouncilFeePaymentRepository feePayRepo;

    private final UserRepository userRepository;          // 학생회장/요청자 조회
    private final AccountApiService accountApiService;    // 외부 잔액 조회


    private static final String PRESIDENT_ID = "solsol5@ssafy.co.kr";
    /**
     * 홈 카드
     * - 잔액: 학생회장 개인 계좌 잔액 사용
     * - 월 지출 합계/회비 배지: 기존 집계 로직 유지
     */
    public DeptHomeSummaryResponse getHomeSummary(
            String requesterUserId, YearMonth ym, String tz, String semesterOpt
    ) {
        // 1) 요청자 기준 활성 학생회 찾기 (프로젝트에 맞는 구현 필요)
        StudentCouncilView council = councilRepo.findByCouncilId(1);

        // 2) 학생회장 사용자 & 계좌 정보 확인
        User president = getActiveUser(PRESIDENT_ID);
        requireUserKeyAndAccount(president);

        // 3) 기간 계산
        ZoneOffset zone = ZoneOffset.of(tz);
        Instant from = ym.atDay(1).atStartOfDay().toInstant(zone);
        Instant to   = ym.plusMonths(1).atDay(1).atStartOfDay().toInstant(zone);

        // 4) 잔액(학생회장 개인 계좌)
        BigDecimal currentBalance = fetchPresidentBalance(president.getUserKey(), president.getAccountNo());

        // 5) 이번 달 지출 합계(학생회 기준)
        BigDecimal monthSpend = expRepo.sumByCouncilAndPeriod(council.councilId(), from, to)
                .orElse(BigDecimal.ZERO);

        // 6) (선택) 회비 배지: 요청자 기준 해당 학기 납부 완료 여부
        DeptHomeSummaryResponse.FeeBadge feeBadge = null;
        if (semesterOpt != null && !semesterOpt.isBlank()) {
            Optional<CouncilFeeView> feeOpt = feeRepo.findByCouncilIdAndSemester(council.councilId(), semesterOpt);
            if (feeOpt.isPresent()) {
                boolean paid = feePayRepo.existsCompletedByFeeIdAndUserId(feeOpt.get().feeId(), requesterUserId);
                feeBadge = new DeptHomeSummaryResponse.FeeBadge(
                        semesterOpt, paid, feeOpt.get().feeAmount(),
                        paid ? feePayRepo.findLatestPaidAt(feeOpt.get().feeId(), requesterUserId).orElse(null) : null
                );
            }
        }

        // departmentId 는 더 이상 사용하지 않으므로 null로 반환(필요 시 DTO 스키마 조정)
        return new DeptHomeSummaryResponse(
                new DeptHomeSummaryResponse.Header(
                        null, // departmentId
                        council.councilId(),
                        council.councilName(),
                        president.getUserId(),
                        president.getName()
                ),
                new DeptHomeSummaryResponse.BalanceCard(currentBalance, monthSpend, ym),
                feeBadge
        );
    }

    /**
     * 지출 리스트
     * - 헤더 잔액: 학생회장 개인 계좌 잔액
     * - 월 지출 합계/목록: 기존 로직 유지
     */
    public DeptExpenditureListResponse getExpenditureList(
            String requesterUserId, YearMonth ym, String tz, int page, int size
    ) {
        // 1) 요청자 기준 활성 학생회
        StudentCouncilView council = councilRepo.findByCouncilId(1);

        // 2) 학생회장 사용자 & 계좌 확인
        User president = getActiveUser(PRESIDENT_ID);
        requireUserKeyAndAccount(president);

        // 3) 기간
        ZoneOffset zone = ZoneOffset.of(tz);
        Instant from = ym.atDay(1).atStartOfDay().toInstant(zone);
        Instant to   = ym.plusMonths(1).atDay(1).atStartOfDay().toInstant(zone);

        // 4) 헤더 잔액(학생회장 계좌)
        BigDecimal currentBalance = fetchPresidentBalance(president.getUserKey(), president.getAccountNo());

        // 5) 월 지출 합계 + 목록
        BigDecimal total = expRepo.sumByCouncilAndPeriod(council.councilId(), from, to)
                .orElse(BigDecimal.ZERO);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "expenditureDate", "expenditureId"));
        Page<CouncilExpenditureRow> p = expRepo.findPageByCouncilAndPeriod(council.councilId(), from, to, pageable);

        List<DeptExpenditureListResponse.Item> items = p.getContent().stream()
                .map(e -> new DeptExpenditureListResponse.Item(
                        e.expenditureId(),
                        e.expenditureDate().atZone(ZoneOffset.UTC).toLocalDate(),
                        e.description(),
                        e.amount()
                )).toList();

        return new DeptExpenditureListResponse(
                new DeptExpenditureListResponse.Header(currentBalance),
                new DeptExpenditureListResponse.Summary(ym, total),
                new DeptExpenditureListResponse.PageMeta(p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages()),
                items
        );
    }

    // ── 내부 유틸 ─────────────────────────────────────
    private User getActiveUser(String userId) {
        return userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }

    private void requireUserKeyAndAccount(User u) {
        if (u.getUserKey() == null || u.getUserKey().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userKey 미설정 사용자");
        if (u.getAccountNo() == null || u.getAccountNo().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "accountNo 미설정 사용자");
    }

    private BigDecimal fetchPresidentBalance(String presidentUserKey, String presidentAccountNo) {
        AccountBalanceResponse res = accountApiService.inquireAccountBalance(presidentUserKey, presidentAccountNo);
        return new BigDecimal(res.getREC().getAccountBalance());
    }
}
