package com.heyyoung.solsol.domain.settlement.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface CouncilFeePaymentRepository extends Repository<Object, Long> {

    @Query("""
        select case when count(p)>0 then true else false end
        from CouncilFeePayment p
        where p.feeId = :feeId and p.userId = :userId and p.paymentStatus = 'COMPLETED'
    """)
    boolean existsCompletedByFeeIdAndUserId(@Param("feeId") Long feeId, @Param("userId") String userId);

    @Query("""
        select max(p.paidAt) from CouncilFeePayment p
        where p.feeId = :feeId and p.userId = :userId and p.paymentStatus = 'COMPLETED'
    """)
    Optional<Instant> findLatestPaidAt(@Param("feeId") Long feeId, @Param("userId") String userId);
}
