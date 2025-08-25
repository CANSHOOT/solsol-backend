package com.heyyoung.solsol.domain.settlement.repository;

import com.heyyoung.solsol.domain.settlement.dto.CouncilFeeView;
import com.heyyoung.solsol.domain.settlement.entity.CouncilFee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CouncilFeeRepository extends Repository<CouncilFee, Long> {

    @Query("""
        select new com.heyyoung.solsol.domain.settlement.dto.CouncilFeeView(
            f.feeId, f.councilId, f.semester, f.feeAmount
        )
        from CouncilFee f
        where f.councilId = :councilId and f.semester = :semester and f.status = 'ACTIVE'
    """)
    Optional<CouncilFeeView> findByCouncilIdAndSemester(@Param("councilId") Long councilId,
                                                        @Param("semester") String semester);
}
