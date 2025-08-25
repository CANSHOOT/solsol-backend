package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.domain.discount.DiscountCouponService;
import com.heyyoung.solsol.domain.menu.MenuService;
import com.heyyoung.solsol.domain.menu.dto.GetMenuResponse;
import com.heyyoung.solsol.domain.payment.dto.CreatePaymentResponse;
import com.heyyoung.solsol.domain.payment.dto.GetPaymentPreviewResponse;
import com.heyyoung.solsol.domain.payment.exception.NotEnoughMoneyException;
import com.heyyoung.solsol.domain.payment.repository.PaymentRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.external.dto.account.AccountTransferResponse;
import com.heyyoung.solsol.external.service.account.AccountApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final DiscountCouponService discountCouponService;
    private final MenuService menuService;
    private final AccountApiService accountApiService;

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

    @Transactional
    public CreatePaymentResponse createPaymentResponse(String userId, BigDecimal amount) {
        User userForPayment = paymentRepository.getUserForPayment(userId);
        // 계좌 조회 후 금액이 괜찮은지 확인
        AccountTransferResponse transferResponse = accountApiService
                .transferAccount("3663422a-ec01-42a0-8ebf-433a4178e9ec", userForPayment.getAccountNo(),
                        "구매", amount.toString(),
                        "0880318577304821", "구매");

        userForPayment.updateAccountBalance(userForPayment.getAccountBalance() - amount.longValue());
        if (transferResponse.getHeader().getResponseCode().equals("A1011")) {
            throw new NotEnoughMoneyException();
        }
        // 계좌 이체 완료 시 랜덤으로 500원 쿠폰 줌
        boolean winning = ThreadLocalRandom.current().nextInt(30) <= 5;
        if (winning && discountCouponService.canIssueCouponToUserByUserId(userForPayment))
            return new CreatePaymentResponse(500, true);

        return new CreatePaymentResponse(0, false);
    }

    // 할인률 정수로 추출
    private int extractPoint(BigDecimal discountRate) {
        int intPart = discountRate.intValue();

        BigDecimal fractional = discountRate.subtract(new BigDecimal(intPart));

        return fractional.movePointRight(discountRate.scale()).intValue();
    }
}
