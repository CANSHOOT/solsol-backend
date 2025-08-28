package com.heyyoung.solsol.domain.payment.dto;

import java.util.List;

public record GetPaymentsResponse(List<GetPaymentResponse> payments) {
}
