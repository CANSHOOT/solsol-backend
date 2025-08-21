package com.heyyoung.solsol.external.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApiKeyReissueResponse {
    private String managerld;
    private String apiKey;
}