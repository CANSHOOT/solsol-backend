package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountBalanceResponse {

    @JsonProperty("Header")
    private CommonHeaderResponse Header;

    @JsonProperty("REC")
    private AccountBalanceRecord REC;
}