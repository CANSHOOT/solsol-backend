package com.heyyoung.solsol.domain.coupon_daily_cap;

import com.heyyoung.solsol.domain.coupon_daily_cap.exception.CouponDailyCapIsNotExistException;
import com.heyyoung.solsol.domain.coupon_daily_cap.repository.CouponDailyCapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CouponDailyCapService {
    private final CouponDailyCapRepository couponDailyCapRepository;

    @Transactional
    public boolean isRemainIssuedCount() {
        CouponDailyCapEntity couponDailyCapEntity = couponDailyCapRepository.
                findByCreatedAtBetween(Instant.now(), Instant.now().plus(1, ChronoUnit.DAYS))
                .orElseThrow(CouponDailyCapIsNotExistException::new);

        if (couponDailyCapEntity.getIssuedCount() == 10)
            return false;

        couponDailyCapEntity.incrementIssuedCount();

        return true;
    }
}
