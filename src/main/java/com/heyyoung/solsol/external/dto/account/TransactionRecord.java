package com.heyyoung.solsol.external.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransactionRecord {
    
    private String transactionUniqueNo;
    private String accountNo;
    private String transactionDate;
    private String transactionType;
    private String transactionTypeName;
    private String transactionAccountNo;
}