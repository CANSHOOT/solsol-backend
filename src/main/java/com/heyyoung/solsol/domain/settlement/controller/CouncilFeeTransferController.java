package com.heyyoung.solsol.domain.settlement.controller;

import com.heyyoung.solsol.domain.settlement.dto.CouncilFeeTransferCommand;
import com.heyyoung.solsol.domain.settlement.service.CouncilFeeTransferService;
import com.heyyoung.solsol.external.dto.account.AccountTransferResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 회비 송금 컨트롤러
 * - userKey: 현재 로그인 사용자 정보에서 조회
 * - depositAccountNo: 학생회(학과) 고정 계좌에서 조회
 */
@RestController
@RequestMapping("/v1/councils")
@RequiredArgsConstructor
@Slf4j
public class CouncilFeeTransferController {

    private final AccountApiService accountApiService;
    private final CouncilFeeTransferService councilFeeTransferService;

    // TODO: 고정 학생회 계좌 분리 필요
    private final static String DEPT_ACCOUNT_NO = "0881018866954932";
    private final static String DEPT_USER_ID = "solsol5@ssafy.co.kr";


    @GetMapping("/all")
    public ResponseEntity<?> getAllCouncilFees() {
        return ResponseEntity.ok(councilFeeTransferService.getAllFees());
    }

    /**
     * 회비 송금
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transferCouncilFee(
            @RequestBody CouncilFeeTransferCommand cmd,
            Authentication auth
    ) {
        String userId = auth.getName();

        // userKey 조회(현재 사용자)
        String userKey = councilFeeTransferService.getUserKeyById(userId);

        String accountNo = councilFeeTransferService.getAccountNoByUser(userId);

        // 출금 계좌 검증
        if (!accountNo.equals(cmd.withdrawalAccountNo())) {
            log.warn("계좌 불일치: 요청 계좌={}, 실제 계좌={}",
                    cmd.withdrawalAccountNo(), accountNo);
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body("요청한 출금 계좌가 사용자 계좌와 일치하지 않습니다.");
        }

        System.out.println(cmd.feeId());
        // 회비 금액 조회
        BigDecimal feeAmount = councilFeeTransferService.getFeeAmountById(cmd.feeId());

        // 요약문 기본값 구성(미지정 시)
        String depositSummary = (cmd.depositTransactionSummary() == null || cmd.depositTransactionSummary().isBlank())
                ? "학생회 회비 입금"
                : cmd.depositTransactionSummary();

        String withdrawalSummary = (cmd.withdrawalTransactionSummary() == null || cmd.withdrawalTransactionSummary().isBlank())
                ? "회비 납부"
                : cmd.withdrawalTransactionSummary();

        // 이체 실행
        AccountTransferResponse res = accountApiService.transferAccount(
                userKey,
                DEPT_ACCOUNT_NO,
                depositSummary,
                feeAmount.toString(),
                cmd.withdrawalAccountNo(),
                withdrawalSummary
        );

        // 이체 후 잔액 업데이트
        councilFeeTransferService.updateBalance(DEPT_USER_ID);
        councilFeeTransferService.updateBalance(userId);

        // 납부 내역 저장
        councilFeeTransferService.recordPayment(
                cmd.feeId(),                // 납부 종류
                userId,                     // 납부 학생
                feeAmount                   // 납부 금액
        );

        return ResponseEntity.ok(res);
    }
}
