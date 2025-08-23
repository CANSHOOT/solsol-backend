package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TransactionHistoryRequest {

    @JsonProperty("Header")
    private CommonHeaderRequest Header;

    private String accountNo;
    private String startDate;
    private String endDate;
    private String transactionType;
    private String orderByType;

    @Builder
    public TransactionHistoryRequest(CommonHeaderRequest Header, String accountNo,
                                   String startDate, String endDate, String transactionType,
                                   String orderByType) {
        this.Header = Header;
        this.accountNo = accountNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.transactionType = transactionType;
        this.orderByType = orderByType;
    }
}