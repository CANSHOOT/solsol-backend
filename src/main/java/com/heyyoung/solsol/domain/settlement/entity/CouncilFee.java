package com.heyyoung.solsol.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "COUNCIL_FEES",
        indexes = {
                @Index(name = "idx_fee_council_semester", columnList = "council_id, semester"),
                @Index(name = "idx_fee_status", columnList = "status")
        }
)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CouncilFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_id")
    private Long feeId;

    @Column(name = "council_id", nullable = false)
    private Long councilId;

    @Column(name = "semester", nullable = false, length = 16) // 예: 2025-2
    private String semester;

    @Column(name = "fee_amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal feeAmount;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "due_date")
    private Instant dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private CouncilFeeStatus status = CouncilFeeStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
