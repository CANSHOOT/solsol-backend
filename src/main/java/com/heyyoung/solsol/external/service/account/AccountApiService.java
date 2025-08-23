package com.heyyoung.solsol.external.service.account;

import com.heyyoung.solsol.external.dto.account.*;
import com.heyyoung.solsol.external.service.FinOpenApiClient;
import com.heyyoung.solsol.external.service.common.CommonHeaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountApiService {

    private final FinOpenApiClient apiClient;
    private final CommonHeaderService commonHeaderService;

    /**
     * 계좌 이체
     */
    public AccountTransferResponse transferAccount(String userKey, String depositAccountNo, 
                                                 String depositTransactionSummary, String transactionBalance,
                                                 String withdrawalAccountNo, String withdrawalTransactionSummary) {
        var header = commonHeaderService.createCommonHeader("updateDemandDepositAccountTransfer", userKey);
        
        AccountTransferRequest request = AccountTransferRequest.builder()
                .Header(header)
                .depositAccountNo(depositAccountNo)
                .depositTransactionSummary(depositTransactionSummary)
                .transactionBalance(transactionBalance)
                .withdrawalAccountNo(withdrawalAccountNo)
                .withdrawalTransactionSummary(withdrawalTransactionSummary)
                .build();

        return apiClient.post("/edu/demandDeposit/updateDemandDepositAccountTransfer", request, AccountTransferResponse.class);
    }

    /**
     * 계좌 잔액 조회
     */
    public AccountBalanceResponse inquireAccountBalance(String userKey, String accountNo) {
        var header = commonHeaderService.createCommonHeader("inquireDemandDepositAccountBalance", userKey);
        
        AccountBalanceRequest request = AccountBalanceRequest.builder()
                .Header(header)
                .accountNo(accountNo)
                .build();

        return apiClient.post("/edu/demandDeposit/inquireDemandDepositAccountBalance", request, AccountBalanceResponse.class);
    }
}