package com.heyyoung.solsol.domain.payment.repository;

import com.heyyoung.solsol.domain.payment.PaymentStatus;
import com.heyyoung.solsol.domain.payment.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentsEntity, Long>, PaymentQueryRepository {
    List<PaymentsEntity> findByUserUserIdAndPaymentStatus(String userId, PaymentStatus status);
}
