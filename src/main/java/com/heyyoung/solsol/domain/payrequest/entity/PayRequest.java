package com.heyyoung.solsol.domain.payrequest.entity;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayGroup;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "pay_requests",
        indexes = {
                @Index(name = "idx_payreq_requester", columnList = "requester_id"),
                @Index(name = "idx_payreq_group", columnList = "group_id")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayRequest extends BaseUpdatedCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_request_id")
    private Long payRequestId;

    // 요청자 (돈 받을 사람: A)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    // 더치페이 기반이면 연결(개인 간이면 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private DutchPayGroup dutchPayGroup;

    // 메모(선택)
    @Column(name = "memo", length = 200)
    private String memo;

    // 전체 청구 합계(선택; 조회 편의용)
    @Column(name = "total_amount", precision = 14, scale = 2)
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "payRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PayRequestParticipant> participants = new ArrayList<>();

    @Builder
    private PayRequest(User requester,
                       DutchPayGroup dutchPayGroup,
                       String memo,
                       BigDecimal totalAmount) {
        this.requester = requester;
        this.dutchPayGroup = dutchPayGroup;
        this.memo = memo;
        this.totalAmount = totalAmount;
    }

    public void addParticipant(PayRequestParticipant p) {
        this.participants.add(p);
        p.bindRequest(this);
    }
}
