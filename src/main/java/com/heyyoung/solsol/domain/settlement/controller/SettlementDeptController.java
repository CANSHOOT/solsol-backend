package com.heyyoung.solsol.domain.settlement.controller;

import com.heyyoung.solsol.domain.settlement.dto.DeptExpenditureListResponse;
import com.heyyoung.solsol.domain.settlement.dto.DeptHomeSummaryResponse;
import com.heyyoung.solsol.domain.settlement.service.SettlementDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/v1/settlement")
@RequiredArgsConstructor
public class SettlementDeptController {

    private final SettlementDeptService service;

    /** 홈 카드: 잔액(학생회장 계좌) + 이번 달 지출 + (선택) 내 회비 배지 */
    @GetMapping("/home")
    public ResponseEntity<DeptHomeSummaryResponse> home(
            @RequestParam(required = false) String month,          // "YYYY-MM"
            @RequestParam(required = false) String semester,       // "YYYY-1|YYYY-2" (선택)
            @RequestParam(defaultValue = "+09:00") String tz,
            Authentication auth
    ) {
        String requesterId = auth.getName(); // String userId 가정
        YearMonth ym = (month == null || month.isBlank()) ? YearMonth.now() : YearMonth.parse(month);
        return ResponseEntity.ok(service.getHomeSummary(requesterId, ym, tz, semester));
    }

    /** 지출 목록: 이번 달 지출 합계 + 리스트 + 헤더 잔액(학생회장 계좌) */
    @GetMapping("/expenditures")
    public ResponseEntity<DeptExpenditureListResponse> expenditures(
            @RequestParam(required = false) String month,          // "YYYY-MM"
            @RequestParam(defaultValue = "+09:00") String tz,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication auth
    ) {
        String requesterId = auth.getName();
        YearMonth ym = (month == null || month.isBlank()) ? YearMonth.now() : YearMonth.parse(month);
        return ResponseEntity.ok(service.getExpenditureList(requesterId, ym, tz, page, size));
    }
}
