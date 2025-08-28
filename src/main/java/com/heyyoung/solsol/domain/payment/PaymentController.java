package com.heyyoung.solsol.domain.payment;

import com.heyyoung.solsol.domain.payment.dto.CreatePaymentRequest;
import com.heyyoung.solsol.domain.payment.dto.CreatePaymentResponse;
import com.heyyoung.solsol.domain.payment.dto.GetPaymentPreviewResponse;
import com.heyyoung.solsol.domain.payment.dto.GetPaymentsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/v1/payments/{paymentId}")
    public ResponseEntity<CreatePaymentResponse> createPayment(@PathVariable long paymentId,
                                                               @RequestBody CreatePaymentRequest createPaymentRequest,
                                                               @AuthenticationPrincipal String user) {
        CreatePaymentResponse response = paymentService.createPaymentResponse(user, createPaymentRequest, paymentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/v1/payments")
    public ResponseEntity<GetPaymentsResponse> getPayments(@AuthenticationPrincipal String user) {
        GetPaymentsResponse response = paymentService.getPayments(user);
        return ResponseEntity.ok(response);
    }
}
