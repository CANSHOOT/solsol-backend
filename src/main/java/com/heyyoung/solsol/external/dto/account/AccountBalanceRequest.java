package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountBalanceRequest {

    @JsonProperty("Header")
    private CommonHeaderRequest Header;

    private String accountNo;

    @Builder
    public AccountBalanceRequest(CommonHeaderRequest Header, String accountNo) {
        this.Header = Header;
        this.accountNo = accountNo;
    }
}