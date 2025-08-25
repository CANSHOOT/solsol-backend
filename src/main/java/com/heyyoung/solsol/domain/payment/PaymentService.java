package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.domain.menu.MenuService;
import com.heyyoung.solsol.domain.menu.dto.GetMenuResponse;
import com.heyyoung.solsol.domain.payment.dto.GetPaymentPreviewResponse;
import com.heyyoung.solsol.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final MenuService menuService;

    @Transactional(readOnly = true)
    public GetPaymentPreviewResponse getPreview(String userId) {
        List<GetMenuResponse> list = menuService.getPayMenus();
        BigDecimal total = new BigDecimal(0);
        for (GetMenuResponse getMenuResponse : list) {
            total = total.add(getMenuResponse.price());
        }
        BigDecimal discountRate = paymentRepository.getDepartment(userId)
                .discountRate();

        BigDecimal discount = total.multiply(discountRate);
        return new GetPaymentPreviewResponse(list, total.intValue(), extractPoint(discountRate), discount.intValue());
    }

    // 할인률 정수로 추출
    private int extractPoint(BigDecimal discountRate) {
        int intPart = discountRate.intValue();

        BigDecimal fractional = discountRate.subtract(new BigDecimal(intPart));

        return fractional.movePointRight(discountRate.scale()).intValue();
    }

}
