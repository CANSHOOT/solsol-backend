package com.heyyoung.solsol.external.dto.account;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountBalanceRecord {
    
    private String bankCode;
    private String accountNo;
    private String accountBalance;
    private String accountCreatedDate;
    private String accountExpiryDate;
    private String lastTransactionDate;
    private String currency;
}