package com.heyyoung.solsol.external.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiKeyReissueRequest {
    private String managerld;
}