package com.heyyoung.solsol.external.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.heyyoung.solsol.external.dto.common.CommonHeaderResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AccountTransferResponse {

    @JsonProperty("Header")
    private CommonHeaderResponse Header;

    @JsonProperty("REC")
    private List<TransactionRecord> REC;
}