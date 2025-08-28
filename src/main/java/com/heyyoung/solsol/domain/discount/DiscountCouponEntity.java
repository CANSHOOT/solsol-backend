package com.heyyoung.solsol.domain.discount;

import com.heyyoung.solsol.common.entity.BaseUpdatedCreatedEntity;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "discount_coupon")
@NoArgsConstructor
@Getter
public class DiscountCouponEntity extends BaseUpdatedCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long discountCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    private BigDecimal amount;

    public void setOwner(User user) {
        this.user = user;
    }

    public void useCoupon(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    @Builder
    public DiscountCouponEntity(User user, CouponStatus couponStatus, BigDecimal amount) {
        this.user = user;
        this.couponStatus = couponStatus;
        this.amount = amount;
    }
}
