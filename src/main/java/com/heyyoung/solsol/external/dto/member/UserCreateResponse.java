package com.heyyoung.solsol.external.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserCreateResponse {
    private String userId;
    private String userName;
    private String institutionCode;
    private String userKey;
    private String created;
    private String modified;
}