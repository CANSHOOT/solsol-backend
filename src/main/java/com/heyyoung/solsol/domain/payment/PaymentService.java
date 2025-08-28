package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.domain.discount.CouponStatus;
import com.heyyoung.solsol.domain.discount.DiscountCouponEntity;
import com.heyyoung.solsol.domain.discount.DiscountCouponService;
import com.heyyoung.solsol.domain.discount.dto.GetDiscountCouponResponse;
import com.heyyoung.solsol.domain.menu.MenuService;
import com.heyyoung.solsol.domain.menu.dto.GetMenuResponse;
import com.heyyoung.solsol.domain.payment.dto.*;
import com.heyyoung.solsol.domain.payment.exception.NotEnoughMoneyException;
import com.heyyoung.solsol.domain.payment.exception.PaymentNotExistException;
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

    @Transactional
    public GetPaymentPreviewResponse getPreview(String userId) {
        List<GetMenuResponse> list = menuService.getPayMenus();
        BigDecimal total = new BigDecimal(0);
        for (GetMenuResponse getMenuResponse : list) {
            total = total.add(getMenuResponse.price());
        }
        GetDepartment department = paymentRepository.getDepartment(userId);
        BigDecimal discountRate = department.discountRate();
        String name = department.departmentName();

        List<GetDiscountCouponResponse> paymentPreviewResponses = discountCouponService
                .getDiscountCouponsForPreview(userId);

        BigDecimal discount = total.multiply(discountRate);

        PaymentsEntity payment = PaymentsEntity
                .builder()
                .paymentStatus(PaymentStatus.PENDING)
                .discountRate(discountRate)
                .discountAmount(discount)
                .originalAmount(total)
                .finalAmount(total.subtract(discount))
                .paymentMethod(PaymentMethod.QR)
                .transactionSummary("구매")
                .apiTransactionId("-1")
                .build();

        long paymentId = paymentRepository.save(payment).getPaymentId();

        return new GetPaymentPreviewResponse(list, total.intValue(), extractPoint(discountRate),
                discount.intValue(), name, paymentPreviewResponses, paymentId);
    }

    @Transactional
    public CreatePaymentResponse createPaymentResponse(String userId,
                                                       CreatePaymentRequest createPaymentRequest,
                                                       long paymentId) {
        User user = paymentRepository.getUserForPayment(userId);
        // 계좌 조회 후 금액이 괜찮은지 확인
        AccountTransferResponse transferResponse = accountApiService
                .transferAccount("3663422a-ec01-42a0-8ebf-433a4178e9ec", user.getAccountNo(),
                        "구매", createPaymentRequest.amount().toString(),
                        "0880318577304821", "구매");

        user
                .updateAccountBalance(user.getAccountBalance() - createPaymentRequest.amount().longValue());
        if (transferResponse.getHeader().getResponseCode().equals("A1011")) {
            throw new NotEnoughMoneyException();
        }

        PaymentsEntity payment = paymentRepository.findById(paymentId)
                .orElseThrow(PaymentNotExistException::new);

        if (createPaymentRequest.discountCouponId() != -1) {
            DiscountCouponEntity discountCoupon = discountCouponService
                    .findByUserUserIdAndDiscountCouponIdAndCreatedAtBetweenAndCouponStatus(userId,
                            createPaymentRequest.discountCouponId());

            discountCoupon.useCoupon(CouponStatus.USED);
            payment.updateAmount(payment.getFinalAmount().subtract(discountCoupon.getAmount()),
                    payment.getDiscountAmount().add(discountCoupon.getAmount()));
        }


        payment.updatePaymentStatus(PaymentStatus.COMPLETED);
        payment.updateApiTransactionId(transferResponse.getREC().getLast().getTransactionUniqueNo());
        payment.setUser(user);

        // 계좌 이체 완료 시 랜덤으로 500원 쿠폰 줌
        boolean winning = ThreadLocalRandom.current().nextInt(30) <= 5;
        if (winning && discountCouponService.canIssueCouponToUserByUserId(user))
            return new CreatePaymentResponse(500, true);

        return new CreatePaymentResponse(0, false);
    }

    public GetPaymentsResponse getPayments(String userId) {
        List<GetPaymentResponse> payments = paymentRepository.findByUserUserIdAndPaymentStatus(userId, PaymentStatus.COMPLETED)
                .stream()
                .map(GetPaymentResponse::from)
                .toList();

        return new GetPaymentsResponse(payments);
    }

    // 할인률 정수로 추출
    private int extractPoint(BigDecimal discountRate) {
        int intPart = discountRate.intValue();

        BigDecimal fractional = discountRate.subtract(new BigDecimal(intPart));

        return fractional.movePointRight(discountRate.scale()).intValue();
    }
}
