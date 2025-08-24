package com.heyyoung.solsol.external.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BankCodeResponse {
    private CommonHeaderResponse Header;
    private List<BankCode> REC;
    
    @Getter
    @NoArgsConstructor
    public static class BankCode {
        private String bankCode;
        private String bankName;
    }
}