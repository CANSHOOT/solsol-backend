package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public record DeptExpenditureListResponse(
        Header header,
        Summary summary,
        PageMeta page,
        List<Item> items
) {
    public record Header(BigDecimal currentBalance) {}
    public record Summary(YearMonth month, BigDecimal monthSpendTotal) {}
    public record PageMeta(int page, int size, long totalElements, int totalPages) {}
    public record Item(Long expenditureId, LocalDate date, String description, BigDecimal amount) {}
}
