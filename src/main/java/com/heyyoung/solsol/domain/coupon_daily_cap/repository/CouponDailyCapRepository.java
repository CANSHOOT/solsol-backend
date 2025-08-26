package com.heyyoung.solsol.domain.coupon_daily_cap.repository;

import com.heyyoung.solsol.domain.coupon_daily_cap.CouponDailyCapEntity;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import java.time.Instant;
import java.util.Optional;

public interface CouponDailyCapRepository extends JpaRepository<CouponDailyCapEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "5000"))
    Optional<CouponDailyCapEntity> findByCreatedAtBetween(Instant now, Instant tomorrow);
}
