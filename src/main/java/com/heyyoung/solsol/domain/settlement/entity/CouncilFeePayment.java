package com.heyyoung.solsol.domain.settlement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
        name = "COUNCIL_FEE_PAYMENTS",
        indexes = {
                @Index(name = "idx_fee_pay_fee_user", columnList = "fee_id, user_id"),
                @Index(name = "idx_fee_pay_status_paidat", columnList = "payment_status, paid_at")
        }
)
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class CouncilFeePayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fee_payment_id")
    private Long feePaymentId;

    @Column(name = "fee_id", nullable = false)
    private Long feeId;

    /** ERD가 string 이므로 String 유지 (예: 이메일 기반 ID) */
    @Column(name = "user_id", nullable = false, length = 128)
    private String userId;

    @Column(name = "amount", precision = 19, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "transaction_id", length = 64)
    private String transactionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20, nullable = false)
    private FeePaymentStatus paymentStatus = FeePaymentStatus.COMPLETED;

    @Column(name = "paid_at", nullable = false)
    private Instant paidAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
