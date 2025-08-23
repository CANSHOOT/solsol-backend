package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountTransferRequest {

    @JsonProperty("Header")
    private CommonHeaderRequest Header;

    private String depositAccountNo;
    private String depositTransactionSummary;
    private String transactionBalance;
    private String withdrawalAccountNo;
    private String withdrawalTransactionSummary;

    @Builder
    public AccountTransferRequest(CommonHeaderRequest Header, String depositAccountNo,
                                String depositTransactionSummary, String transactionBalance,
                                String withdrawalAccountNo, String withdrawalTransactionSummary) {
        this.Header = Header;
        this.depositAccountNo = depositAccountNo;
        this.depositTransactionSummary = depositTransactionSummary;
        this.transactionBalance = transactionBalance;
        this.withdrawalAccountNo = withdrawalAccountNo;
        this.withdrawalTransactionSummary = withdrawalTransactionSummary;
    }
}