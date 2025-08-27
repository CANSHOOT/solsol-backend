package com.heyyoung.solsol.domain.settlement.repository;

import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureRow;
import com.heyyoung.solsol.domain.settlement.entity.CouncilExpenditure;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public interface CouncilExpenditureRepository extends JpaRepository<CouncilExpenditure, Long> {

    @Query("""
        select coalesce(sum(e.amount),0)
        from CouncilExpenditure e
        where e.councilId = :councilId
          and e.expenditureDate >= :from and e.expenditureDate < :to
    """)
    Optional<BigDecimal> sumByCouncilAndPeriod(@Param("councilId") Long councilId,
                                               @Param("from") Instant from,
                                               @Param("to") Instant to);

    @Query("""
        select new com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureRow(
            e.expenditureId, e.councilId, e.amount, e.category, e.description, e.expenditureDate
        )
        from CouncilExpenditure e
        where e.councilId = :councilId
        order by e.expenditureDate desc
    """)
    Page<CouncilExpenditureRow> findPageByCouncil(@Param("councilId") Long councilId, Pageable pageable);
}
