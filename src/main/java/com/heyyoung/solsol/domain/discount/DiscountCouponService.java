package com.heyyoung.solsol.domain.discount;

import com.heyyoung.solsol.domain.coupon_daily_cap.CouponDailyCapService;
import com.heyyoung.solsol.domain.discount.repository.DiscountCouponRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DiscountCouponService {
    private final DiscountCouponRepository discountCouponRepository;
    private final CouponDailyCapService couponDailyCapService;

    public boolean canIssueCouponToUserByUserId(User user) {
        boolean alreadyReceiveCouponToday = discountCouponRepository.existsByUserUserId(user.getUserId());
        boolean isRemainIssueCount = couponDailyCapService.isRemainIssuedCount();

        if (alreadyReceiveCouponToday || !isRemainIssueCount) return false;

        DiscountCouponEntity discountCoupon = DiscountCouponEntity.builder()
                .couponStatus(CouponStatus.AVAILABLE)
                .user(user)
                .amount(new BigDecimal("500"))
                .build();

        discountCouponRepository.save(discountCoupon);

        return true;
    }
}

