package com.heyyoung.solsol.domain.payment.repository;

import com.heyyoung.solsol.domain.department.entity.QDepartment;
import com.heyyoung.solsol.domain.payment.dto.GetDepartment;
import com.heyyoung.solsol.domain.user.entity.QUser;
import com.heyyoung.solsol.domain.user.entity.User;
import com.querydsl.core.types.Projections;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class PaymentQueryRepositoryImpl extends QuerydslRepositorySupport implements PaymentQueryRepository {

    public PaymentQueryRepositoryImpl() {
        super(PaymentQueryRepositoryImpl.class);
    }

    @Override
    public GetDepartment getDepartment(String userId) {
        QUser user = QUser.user;
        QDepartment department = QDepartment.department;

        return from(user)
                .join(department).on(user.department.departmentId.eq(department.departmentId))
                .where(user.userId.eq(userId))
                .where(department.isPartner.eq(true))
                .select(Projections.constructor(GetDepartment.class,
                        department.discountRate,
                        department.departmentName
                )).fetchOne();
    }

    @Override
    public User getUserForPayment(String userId) {
        QUser user = QUser.user;

        return from(user).select(user)
                .where(user.userId.eq(userId)).fetchOne();
    }
}
