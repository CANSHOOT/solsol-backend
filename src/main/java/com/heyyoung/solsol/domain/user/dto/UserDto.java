package com.heyyoung.solsol.domain.user.dto;

import com.heyyoung.solsol.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

/**
 * 사용자 정보 DTO
 */
@Getter
@Builder
public class UserDto {
    
    private String userId;
    private String studentNumber;
    private String name;
    private Long departmentId;
    private String departmentName;
    private Long councilId;
    private boolean isCouncilOfficer;
    private String accountNo;
    private Long accountBalance;

    /**
     * User 엔티티를 UserDto로 변환
     * @param user 사용자 엔티티
     * @return UserDto
     */
    public static UserDto from(User user) {
        return UserDto.builder()
                .userId(user.getUserId())
                .studentNumber(user.getStudentNumber())
                .name(user.getName())
                .departmentId(user.getDepartment().getDepartmentId())
                .departmentName(user.getDepartment().getDepartmentName())
                .councilId(user.getCouncilId())
                .isCouncilOfficer(user.getIsCouncilOfficer())
                .accountNo(user.getAccountNo())
                .accountBalance(user.getAccountBalance())
                .build();
    }
}