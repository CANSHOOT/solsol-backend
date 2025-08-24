package com.heyyoung.solsol.external.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BankCodeRequest {
    private CommonHeaderRequest Header;
}