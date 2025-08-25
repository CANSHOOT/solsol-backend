package com.heyyoung.solsol.domain.settlement.controller;

import com.heyyoung.solsol.domain.settlement.dto.FeeStatusResponse;
import com.heyyoung.solsol.domain.settlement.service.FeeStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/settlement")
@RequiredArgsConstructor
public class FeeStatusController {

    private final FeeStatusService service;

    /**
     * 특정 학생회(councilId)의 특정 회비(feeId)에 대한 전체 납부 현황
     * 예) GET /v1/settlement/councils/10001/fees/20002/status
     */
    @GetMapping("/councils/{councilId}/fees/{feeId}/status")
    public ResponseEntity<FeeStatusResponse> getCouncilFeeStatus(
            @PathVariable Long councilId,
            @PathVariable Long feeId
    ) {
        return ResponseEntity.ok(service.getFeeStatusByCouncil(councilId, feeId));
    }
}
