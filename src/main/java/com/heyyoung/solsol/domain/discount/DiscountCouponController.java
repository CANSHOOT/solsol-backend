package com.heyyoung.solsol.domain.discount;

import com.heyyoung.solsol.domain.discount.dto.GetDiscountCouponsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscountCouponController {
    private final DiscountCouponService discountCouponService;

    @GetMapping("/v1/discounts")
    public ResponseEntity<GetDiscountCouponsResponse> getDiscountCoupons(@AuthenticationPrincipal String userId) {
        GetDiscountCouponsResponse response = discountCouponService.getDiscountCoupons(userId);
        return ResponseEntity.ok(response);
    }
}
