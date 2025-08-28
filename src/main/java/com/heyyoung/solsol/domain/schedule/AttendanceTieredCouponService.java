package com.heyyoung.solsol.domain.schedule;

import com.heyyoung.solsol.domain.discount.repository.DiscountCouponQueryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceTieredCouponService {

    private final DiscountCouponQueryRepository couponQueryRepo;

    @Transactional
    public int issueDailyCouponsTiered() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
        Instant now = Instant.now();

        int total = 0;
        total += couponQueryRepo.issueCouponBucket(today, now, 91.0, null, new BigDecimal("500.00")); // 91~100%
        total += couponQueryRepo.issueCouponBucket(today, now, 80.0, 90.0, new BigDecimal("100.00")); // 80~90%
        log.info("Issued {} attendance coupons (tiered)", total);
        return total;
    }
}
