package com.heyyoung.solsol.domain.coupon_daily_cap;

import com.heyyoung.solsol.common.entity.BaseCreatedEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "coupon_daily_cap")
public class CouponDailyCapEntity extends BaseCreatedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponDailyId;

    private int issuedCount;

    public void incrementIssuedCount() {
        this.issuedCount++;
    }
}
