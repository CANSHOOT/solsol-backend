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

    /**
     * 계좌 거래 내역 조회
     */
    public TransactionHistoryResponse inquireTransactionHistory(String userKey, String accountNo,
                                                              String startDate, String endDate,
                                                              String transactionType, String orderByType) {
        var header = commonHeaderService.createCommonHeader("inquireTransactionHistoryList", userKey);
        
        TransactionHistoryRequest request = TransactionHistoryRequest.builder()
                .Header(header)
                .accountNo(accountNo)
                .startDate(startDate)
                .endDate(endDate)
                .transactionType(transactionType)
                .orderByType(orderByType)
                .build();

        return apiClient.post("/edu/demandDeposit/inquireTransactionHistoryList", request, TransactionHistoryResponse.class);
    }

    /**
     * 계좌 거래 내역 단건 조회
     */
    public SingleTransactionResponse inquireSingleTransaction(String userKey, String accountNo, String transactionUniqueNo) {
        var header = commonHeaderService.createCommonHeader("inquireTransactionHistory", userKey);
        
        SingleTransactionRequest request = SingleTransactionRequest.builder()
                .Header(header)
                .accountNo(accountNo)
                .transactionUniqueNo(transactionUniqueNo)
                .build();

        return apiClient.post("/edu/demandDeposit/inquireTransactionHistory", request, SingleTransactionResponse.class);
    }
}