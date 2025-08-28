package com.heyyoung.solsol.domain.discount.repository;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

@Repository
public interface DiscountCouponQueryRepository {
    int issueCouponBucket(LocalDate today, Instant now, double minInclusive,
                          Double maxInclusiveOrNull, BigDecimal amount);
}
