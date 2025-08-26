package com.heyyoung.solsol.domain.settlement.controller;

import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureRequest;
import com.heyyoung.solsol.domain.settlement.dto.CouncilExpenditureResponse;
import com.heyyoung.solsol.domain.settlement.service.CouncilExpenditureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/council-expenditures")
@RequiredArgsConstructor
public class CouncilExpenditureController {

    private final CouncilExpenditureService expenditureService;


    // 학생회 지출 등록
    @PostMapping
    public ResponseEntity<CouncilExpenditureResponse> registerExpenditure(
            @RequestBody CouncilExpenditureRequest request
    ) {
        CouncilExpenditureResponse response = expenditureService.registerExpenditure(request);
        return ResponseEntity.ok(response);
    }
}
