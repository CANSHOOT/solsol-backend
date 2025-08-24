package com.heyyoung.solsol.domain.dutchpay.entity;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 더치페이 참여자 엔티티
 */
@Entity
@Table(name = "dutch_pay_participants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DutchPayParticipant extends BaseUpdatedCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private DutchPayGroup dutchPayGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "join_method", nullable = false)
    private JoinMethod joinMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private ParticipantPaymentStatus paymentStatus;

    @Column(name = "transfer_transaction_id")
    private String transferTransactionId;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Builder
    public DutchPayParticipant(DutchPayGroup dutchPayGroup, User user, JoinMethod joinMethod) {
        this.dutchPayGroup = dutchPayGroup;
        this.user = user;
        this.joinMethod = joinMethod;
        this.paymentStatus = ParticipantPaymentStatus.PENDING;
        this.joinedAt = LocalDateTime.now();
    }

    /**
     * 결제 완료 처리
     * @param transferTransactionId 송금 거래 ID
     */
    public void completePayment(String transferTransactionId) {
        this.transferTransactionId = transferTransactionId;
        this.paymentStatus = ParticipantPaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    /**
     * 결제 실패 처리
     */
    public void failPayment() {
        this.paymentStatus = ParticipantPaymentStatus.FAILED;
    }
}