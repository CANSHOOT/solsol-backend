package com.heyyoung.solsol.domain.payment.dto;

import com.heyyoung.solsol.domain.menu.dto.GetMenuResponse;

import java.util.List;

public record GetPaymentPreviewResponse(List<GetMenuResponse> orderItems, int total, int discountRate, int discount) {
}
