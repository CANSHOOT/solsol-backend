package com.heyyoung.solsol.domain.dutchpay.entity;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 더치페이 그룹 엔티티
 */
@Entity
@Table(name = "dutch_pay_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DutchPayGroup extends BaseUpdatedCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long groupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Column(name = "total_amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "participant_count", nullable = false)
    private Integer participantCount;

    @Column(name = "amount_per_person", precision = 15, scale = 2, nullable = false)
    private BigDecimal amountPerPerson;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DutchPayStatus status;

    @OneToMany(mappedBy = "dutchPayGroup", cascade = CascadeType.ALL)
    private List<DutchPayParticipant> participants = new ArrayList<>();

    @Builder
    public DutchPayGroup(User organizer, Long paymentId, String groupName, 
                        BigDecimal totalAmount, Integer participantCount) {
        this.organizer = organizer;
        this.paymentId = paymentId;
        this.groupName = groupName;
        this.totalAmount = totalAmount;
        this.participantCount = participantCount;
        this.amountPerPerson = totalAmount.divide(BigDecimal.valueOf(participantCount), 2, BigDecimal.ROUND_HALF_UP);
        this.status = DutchPayStatus.ACTIVE;
    }

    /**
     * 더치페이 상태 변경
     * @param status 새로운 상태
     */
    public void updateStatus(DutchPayStatus status) {
        this.status = status;
    }

    /**
     * 참여자 수 및 1인당 금액 업데이트
     * @param participantCount 참여자 수
     */
    public void updateParticipantCount(Integer participantCount) {
        this.participantCount = participantCount;
        this.amountPerPerson = this.totalAmount.divide(BigDecimal.valueOf(participantCount), 2, BigDecimal.ROUND_HALF_UP);
    }
}