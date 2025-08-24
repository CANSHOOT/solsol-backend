package com.heyyoung.solsol.domain.dutchpay.entity;

/**
 * 더치페이 상태 열거형
 */
public enum DutchPayStatus {
    /**
     * 활성 (참여자 모집 중)
     */
    ACTIVE,
    
    /**
     * 완료 (모든 참여자 결제 완료)
     */
    COMPLETED,
    
    /**
     * 취소
     */
    CANCELLED
}