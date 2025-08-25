package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.domain.payment.dto.GetPaymentPreviewResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @GetMapping("/v1/payments/preview/{tempId}")
    public ResponseEntity<GetPaymentPreviewResponse> getPaymentPreview(@PathVariable long tempId,
                                                                       @AuthenticationPrincipal String user) {
        GetPaymentPreviewResponse response = paymentService.getPreview(user);
        return ResponseEntity.ok(response);
    }
}
