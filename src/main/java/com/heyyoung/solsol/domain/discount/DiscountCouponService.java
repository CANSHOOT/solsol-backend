package com.heyyoung.solsol.domain.discount;

import com.heyyoung.solsol.domain.coupon_daily_cap.CouponDailyCapService;
import com.heyyoung.solsol.domain.discount.dto.GetDiscountCouponResponse;
import com.heyyoung.solsol.domain.discount.dto.GetDiscountCouponsResponse;
import com.heyyoung.solsol.domain.discount.repository.DiscountCouponRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

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

    public GetDiscountCouponsResponse getDiscountCoupons(String userId) {
        List<GetDiscountCouponResponse> coupons = discountCouponRepository
                .findByUserUserIdAndCouponStatusAndCreatedAtBetween(userId, CouponStatus.AVAILABLE,
                        Instant.now().minus(30, ChronoUnit.DAYS), Instant.now())
                .stream()
                .map(coupon ->
                        new GetDiscountCouponResponse(coupon.getDiscountCouponId(),
                                coupon.getAmount().intValue(),
                                coupon.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate(),
                                coupon.getCreatedAt().plus(30, ChronoUnit.DAYS)
                                        .atZone(ZoneId.systemDefault()).toLocalDate()))
                .toList();

        return new GetDiscountCouponsResponse(coupons);
    }
}

