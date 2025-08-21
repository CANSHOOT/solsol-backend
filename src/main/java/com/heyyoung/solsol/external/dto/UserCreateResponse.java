package com.heyyoung.solsol.external.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserCreateResponse {
    private String userld;
    private String userName;
    private String institutionCode;
    private String userKey;
    private LocalDateTime created;
    private LocalDateTime modified;
}