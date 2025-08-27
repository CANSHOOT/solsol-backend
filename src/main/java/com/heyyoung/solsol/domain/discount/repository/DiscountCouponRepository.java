package com.heyyoung.solsol.domain.discount.repository;

import com.heyyoung.solsol.domain.discount.CouponStatus;
import com.heyyoung.solsol.domain.discount.DiscountCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface DiscountCouponRepository extends JpaRepository<DiscountCouponEntity, Long> {
    boolean existsByUserUserId(String userId);

    List<DiscountCouponEntity> findByUserUserIdAndCouponStatusAndCreatedAtBetween(String userId, CouponStatus couponStatus,
                                                                                  Instant from, Instant to);
}
