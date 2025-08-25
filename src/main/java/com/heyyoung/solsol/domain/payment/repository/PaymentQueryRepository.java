package com.heyyoung.solsol.domain.payment.repository;

import com.heyyoung.solsol.domain.payment.dto.GetDepartment;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentQueryRepository {
    GetDepartment getDepartment(String userId);
}
