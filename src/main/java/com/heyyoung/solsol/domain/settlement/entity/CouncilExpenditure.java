package com.heyyoung.solsol.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "COUNCIL_EXPENDITURES",
        indexes = {
                @Index(name = "idx_exp_council_date", columnList = "council_id, expenditure_date"),
                @Index(name = "idx_exp_approved",     columnList = "approved_by"),
                @Index(name = "idx_exp_category",     columnList = "category")
        }
)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CouncilExpenditure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expenditure_id")
    private Long expenditureId;

    @Column(name = "council_id", nullable = false)
    private Long councilId;

    @Column(name = "account_id")
    private Long accountId;

    /** 프로젝트가 이메일 기반이면 String, DB가 BIGINT면 Long으로 변경 */
    @Column(name = "approved_by", nullable = false, length = 128)
    private String approvedBy;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount; // 양수 저장(지출), UI에서 부호 표기

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "receipt_image_url", length = 512)
    private String receiptImageUrl;

    @Column(name = "receipt_number", length = 64)
    private String receiptNumber;

    @Column(name = "transaction_id", length = 64)
    private String transactionId;

    @Column(name = "expenditure_date", nullable = false)
    private Instant expenditureDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}

