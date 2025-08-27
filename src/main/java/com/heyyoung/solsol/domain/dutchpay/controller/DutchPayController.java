package com.heyyoung.solsol.domain.dutchpay.controller;

import com.heyyoung.solsol.domain.dutchpay.dto.*;
import com.heyyoung.solsol.domain.dutchpay.service.DutchPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 더치페이 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/v1/dutchpay")
@RequiredArgsConstructor
public class DutchPayController {

    private final DutchPayService dutchPayService;

    /**
     * 더치페이 생성
     * @param request 더치페이 생성 요청
     * @param auth 인증 정보
     * @return 생성된 더치페이 정보
     */
    @PostMapping
    public ResponseEntity<DutchPayResponse> createDutchPay(
            @RequestBody CreateDutchPayRequest request,
            Authentication auth
    ) {
        String userId = auth.getName();
        DutchPayResponse response = dutchPayService.createDutchPay(request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 더치페이 조회
     * @param groupId 더치페이 그룹 ID
     * @return 더치페이 상세 정보
     */
    @GetMapping("/{groupId}")
    public ResponseEntity<DutchPayResponse> getDutchPayById(@PathVariable Long groupId) {
        DutchPayResponse response = dutchPayService.getDutchPayById(groupId);
        return ResponseEntity.ok(response);
    }

    /**
     * 더치페이 참여
     * @param groupId 더치페이 그룹 ID
     * @param request 참여 요청
     * @param auth 인증 정보
     * @return 참여자 정보
     */
    @PostMapping("/{groupId}/join/{userId}")
    public ResponseEntity<ParticipantResponse> joinDutchPay(
            @PathVariable Long groupId,
            @PathVariable String userId,
            @RequestBody JoinDutchPayRequest request,
            Authentication auth
    ) {
        ParticipantResponse response = dutchPayService.joinDutchPay(groupId, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 송금 처리 (금융 API 호출)
     * @param groupId 더치페이 그룹 ID
     * @param request 송금 요청
     * @param auth 인증 정보
     * @return 송금 처리 결과
     */
    @PostMapping("/{groupId}/pay")
    public ResponseEntity<PaymentResponse> sendPayment(
            @PathVariable Long groupId,
            @RequestBody SendPaymentRequest request,
            Authentication auth
    ) {
        String userId = auth.getName();
        PaymentResponse response = dutchPayService.sendPayment(groupId, request, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자별 더치페이 히스토리
     * @param userId 사용자 ID
     * @param pageable 페이징 정보
     * @return 더치페이 히스토리 목록
     */
    @GetMapping("/history/{userId}")
    public ResponseEntity<List<DutchPayResponse>> getDutchPayHistory(
            @PathVariable String userId,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        List<DutchPayResponse> response = dutchPayService.getDutchPayHistory(userId, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 참여 중인 더치페이 목록
     * @param userId 사용자 ID
     * @return 참여 중인 더치페이 목록
     */
    @GetMapping("/participations/{userId}")
    public ResponseEntity<List<ParticipantResponse>> getUserParticipations(@PathVariable String userId) {
        List<ParticipantResponse> response = dutchPayService.getUserParticipations(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * 더치페이 초대 (푸시 알림 전송)
     * @param groupId 더치페이 그룹 ID
     * @param request 초대 요청
     * @param auth 인증 정보
     * @return 초대 결과
     */
    @PostMapping("/{groupId}/invite")
    public ResponseEntity<String> inviteUsersToDutchPay(
            @PathVariable Long groupId,
            @RequestBody InviteUsersRequest request,
            Authentication auth
    ) {
        String organizerId = auth.getName();
        dutchPayService.inviteUsersToDutchPay(groupId, request.getUserIds(), organizerId);
        return ResponseEntity.ok("초대 알림이 전송되었습니다.");
    }
}