package com.heyyoung.solsol.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(
        name = "STUDENT_COUNCILS",
        indexes = {
                @Index(name = "idx_council_dept_active", columnList = "department_id, is_active")
        }
)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class StudentCouncil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "council_id")
    private Long councilId;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "account_id")
    private Long accountId; // 학생회 전용 계좌(있으면 사용)

    /** 프로젝트가 이메일 기반이면 String, DB가 BIGINT면 Long으로 변경하세요. */
    @Column(name = "president_id", nullable = false, length = 128)
    private String presidentId;

    @Column(name = "council_name", nullable = false, length = 100)
    private String councilName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = Boolean.TRUE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
