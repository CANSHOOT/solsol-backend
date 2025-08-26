package com.heyyoung.solsol.domain.settlement.repository;

import com.heyyoung.solsol.domain.settlement.dto.StudentCouncilView;
import com.heyyoung.solsol.domain.settlement.entity.StudentCouncil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentCouncilRepository extends JpaRepository<StudentCouncil, Long> {
    // 필요한 조회만 정의 (엔티티 보유 여부 상관없이 Projection으로 사용)
    @Query("""
        select new com.heyyoung.solsol.domain.settlement.dto.StudentCouncilView(
            c.councilId, c.departmentId, c.accountId, c.presidentId, c.councilName, c.isActive
        )
        from StudentCouncil c
        where c.departmentId = :deptId and c.isActive = true
    """)
    Optional<StudentCouncilView> findActiveByDepartmentId(@Param("deptId") Long departmentId);

    Optional<StudentCouncil> findByCouncilId(Long i);
}
