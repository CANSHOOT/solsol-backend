package com.heyyoung.solsol.domain.settlement.controller;

import com.heyyoung.solsol.domain.settlement.dto.CouncilFeeTransferCommand;
import com.heyyoung.solsol.domain.settlement.service.CouncilFeeTransferService;
import com.heyyoung.solsol.domain.user.service.UserService;
import com.heyyoung.solsol.external.dto.account.AccountTransferResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
                cmd.transactionBalance(),
                cmd.withdrawalAccountNo(),
                withdrawalSummary
        );

        return ResponseEntity.ok(res);
    }
}
