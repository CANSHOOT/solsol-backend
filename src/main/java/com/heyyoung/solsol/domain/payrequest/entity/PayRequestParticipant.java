package com.heyyoung.solsol.domain.payrequest.entity;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(
        name = "pay_request_participants",
        indexes = {
                @Index(name = "idx_payreq_part_user", columnList = "user_id"),
                @Index(name = "idx_payreq_part_request", columnList = "pay_request_id"),
                @Index(name = "idx_payreq_part_status", columnList = "status")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequestParticipant extends BaseUpdatedCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_request_participant_id")
    private Long payRequestParticipantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_request_id", nullable = false)
    private PayRequest payRequest;

    // 송금자(채무자): B, C …
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // B(또는 C)가 내야 할 금액
    @Column(name = "amount", precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private PayParticipantStatus status;

    // 실제 이체 성공 시 외부거래 고유번호(멱등/추적)
    @Column(name = "transfer_unique_no", length = 64)
    private String transferUniqueNo;

    @Builder
    private PayRequestParticipant(PayRequest payRequest,
                                  User user,
                                  BigDecimal amount,
                                  PayParticipantStatus status,
                                  String transferUniqueNo) {
        this.payRequest = payRequest;
        this.user = user;
        this.amount = amount;
        this.status = status == null ? PayParticipantStatus.REQUESTED : status;
        this.transferUniqueNo = transferUniqueNo;
    }

    void bindRequest(PayRequest request) {
        this.payRequest = request;
    }

    public void markPaid(String transferUniqueNo) {
        this.status = PayParticipantStatus.PAID;
        this.transferUniqueNo = transferUniqueNo;
    }
}
