package com.heyyoung.solsol.domain.settlement.service;

import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureRequest;
import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureResponse;
import com.heyyoung.solsol.domain.settlement.entity.CouncilExpenditure;
import com.heyyoung.solsol.domain.settlement.repository.CouncilExpenditureRepository;
import com.heyyoung.solsol.domain.settlement.entity.StudentCouncil;
import com.heyyoung.solsol.domain.settlement.repository.StudentCouncilRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouncilExpenditureService {

    private final CouncilExpenditureRepository expenditureRepository;
    private final StudentCouncilRepository councilRepository;

    @Transactional
    public CouncilExpenditureResponse registerExpenditure(CouncilExpenditureRequest request) {
        StudentCouncil council = councilRepository.findById(request.councilId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생회 ID"));

        CouncilExpenditure expenditure = CouncilExpenditure.builder()
                .councilId(council.getCouncilId())
                .accountId(council.getAccountId())
                .amount(request.amount())
                .description(request.description())
                .expenditureDate(request.expenditureDate())
                .build();

        CouncilExpenditure saved = expenditureRepository.save(expenditure);

        return new CouncilExpenditureResponse(
                saved.getExpenditureId(),
                council.getCouncilId(),
                saved.getAmount(),
                saved.getDescription(),
                saved.getExpenditureDate(),
                saved.getCreatedAt()
        );
    }
}
