package com.heyyoung.solsol.external.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserErrorResponse {
    private String responseCode;
    private String responseMessage;
}