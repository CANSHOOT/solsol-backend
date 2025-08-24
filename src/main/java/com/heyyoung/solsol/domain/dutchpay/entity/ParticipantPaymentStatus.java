package com.heyyoung.solsol.domain.dutchpay.entity;

/**
 * 참여자 결제 상태 열거형
 */
public enum ParticipantPaymentStatus {
    /**
     * 결제 대기
     */
    PENDING,
    
    /**
     * 결제 완료
     */
    COMPLETED,
    
    /**
     * 결제 실패
     */
    FAILED
}