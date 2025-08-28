package com.heyyoung.solsol.domain.user.entity;

import com.heyyoung.solsol.domain.department.entity.Department;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 사용자 엔티티
 * 학생 정보와 학생회 관련 정보를 포함
 * Soft Delete 방식으로 탈퇴 처리
 */
@Entity
@Table(name = "users", indexes = {
    @Index(name = "idx_users_council_role", columnList = "council_id, is_council_officer")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "user_key", length = 50, nullable = false, unique = true)
    private String userKey;

    @Column(name = "student_number", length = 20, nullable = false, unique = true)
    private String studentNumber;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "council_id")
    private Long councilId;

    @Column(name = "is_council_officer", nullable = false)
    private Boolean isCouncilOfficer = false;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "account_balance")
    private Long accountBalance;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "fcm_token", length = 1024)
    private String fcmToken;

    private double attendanceRate;

    @Builder
    public User(String userId, String userKey, String studentNumber, String name, 
                Department department, Long councilId, Boolean isCouncilOfficer,
                String accountNo, Long accountBalance,  String fcmToken) {
        this.userId = userId;
        this.userKey = userKey;
        this.studentNumber = studentNumber;
        this.name = name;
        this.department = department;
        this.councilId = councilId;
        this.isCouncilOfficer = isCouncilOfficer != null ? isCouncilOfficer : false;
        this.accountNo = accountNo;
        this.accountBalance = accountBalance != null ? accountBalance : 0L;
        this.fcmToken = fcmToken;
    }

    /**
     * 사용자 탈퇴 여부 확인
     * @return 탈퇴된 사용자면 true
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * 사용자 탈퇴 처리 (Soft Delete)
     * deletedAt 필드에 현재 시간을 설정
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 계좌 잔액 업데이트
     * @param newBalance 새로운 잔액
     */
    public void updateAccountBalance(Long newBalance) {
        this.accountBalance = newBalance;
    }

    /**
     * 계좌 정보 설정 (회원가입 시 사용)
     * @param accountNo 계좌번호
     * @param accountBalance 초기 잔액
     */
    public void setAccountInfo(String accountNo, Long accountBalance) {
        this.accountNo = accountNo;
        this.accountBalance = accountBalance;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public boolean hasFcmToken() {
        return this.fcmToken != null && !this.fcmToken.trim().isEmpty();
    }
}