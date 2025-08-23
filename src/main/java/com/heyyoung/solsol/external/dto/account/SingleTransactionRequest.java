package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SingleTransactionRequest {

    @JsonProperty("Header")
    private CommonHeaderRequest Header;

    private String accountNo;
    private String transactionUniqueNo;

    @Builder
    public SingleTransactionRequest(CommonHeaderRequest Header, String accountNo, String transactionUniqueNo) {
        this.Header = Header;
        this.accountNo = accountNo;
        this.transactionUniqueNo = transactionUniqueNo;
    }
}