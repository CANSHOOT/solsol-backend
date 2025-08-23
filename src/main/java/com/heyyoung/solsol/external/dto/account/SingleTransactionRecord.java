package com.heyyoung.solsol.external.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SingleTransactionRecord {
    
    private String transactionUniqueNo;
    private String transactionDate;
    private String transactionTime;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
    private String transactionBalance;
    private String transactionAfterBalance;
    private String transactionSummary;
    private String transactionMemo;
}