package com.heyyoung.solsol.domain.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String studentNumber;
    private String name;
    private Long departmentId;
    private Long councilId;
    private Boolean isCouncilOfficer;
}