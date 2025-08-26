package com.heyyoung.solsol.domain.payment.repository;

import com.heyyoung.solsol.domain.payment.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentsEntity, Long>, PaymentQueryRepository {
}
