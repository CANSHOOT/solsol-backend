package com.heyyoung.solsol.domain.dutchpay.dto;

import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayParticipant;
import com.heyyoung.solsol.domain.dutchpay.entity.JoinMethod;
import com.heyyoung.solsol.domain.dutchpay.entity.ParticipantPaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 더치페이 참여자 응답 DTO
 */
@Getter
@Builder
public class ParticipantResponse {
    
    private Long participantId;
    private Long groupId;
    private String userId;
    private String userName;
    private JoinMethod joinMethod;
    private ParticipantPaymentStatus paymentStatus;
    private String transferTransactionId;
    private LocalDateTime joinedAt;
    private LocalDateTime paidAt;

    /**
     * DutchPayParticipant 엔티티를 ParticipantResponse로 변환
     * @param participant 참여자 엔티티
     * @return ParticipantResponse
     */
    public static ParticipantResponse from(DutchPayParticipant participant) {
        return ParticipantResponse.builder()
                .participantId(participant.getParticipantId())
                .groupId(participant.getDutchPayGroup().getGroupId())
                .userId(participant.getUser().getUserId())
                .userName(participant.getUser().getName())
                .joinMethod(participant.getJoinMethod())
                .paymentStatus(participant.getPaymentStatus())
                .transferTransactionId(participant.getTransferTransactionId())
                .joinedAt(participant.getJoinedAt())
                .paidAt(participant.getPaidAt())
                .build();
    }
}