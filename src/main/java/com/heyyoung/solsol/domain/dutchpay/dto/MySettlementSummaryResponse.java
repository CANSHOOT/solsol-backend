package com.heyyoung.solsol.domain.dutchpay.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public record MySettlementSummaryResponse(
        List<MyPayableItemResponse> payables,
        List<MyReceivableItemResponse> receivables,
        BigDecimal payableTotal,
        BigDecimal receivableTotal
) {
    public static MySettlementSummaryResponse of(
            List<MyPayableItemResponse> payables,
            List<MyReceivableItemResponse> receivables
    ) {
        BigDecimal payableTotal = payables == null ? BigDecimal.ZERO :
                payables.stream().map(MyPayableItemResponse::settlementAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal receivableTotal = receivables == null ? BigDecimal.ZERO :
                receivables.stream().map(MyReceivableItemResponse::settlementAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new MySettlementSummaryResponse(payables, receivables, payableTotal, receivableTotal);
    }
}
