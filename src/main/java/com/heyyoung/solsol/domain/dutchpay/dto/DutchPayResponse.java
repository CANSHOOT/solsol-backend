package com.heyyoung.solsol.domain.dutchpay.dto;

import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayGroup;
import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 더치페이 응답 DTO
 */
@Getter
@Builder
public class DutchPayResponse {
    
    private Long groupId;
    private String organizerId;
    private String organizerName;
    private Long paymentId;
    private String groupName;
    private BigDecimal totalAmount;
    private Integer participantCount;
    private BigDecimal amountPerPerson;
    private DutchPayStatus status;
    private List<ParticipantResponse> participants;
    private Instant createdAt;
    private Instant updatedAt;

    /**
     * DutchPayGroup 엔티티를 DutchPayResponse로 변환
     * @param dutchPayGroup 더치페이 그룹 엔티티
     * @return DutchPayResponse
     */
    public static DutchPayResponse from(DutchPayGroup dutchPayGroup) {
        return DutchPayResponse.builder()
                .groupId(dutchPayGroup.getGroupId())
                .organizerId(dutchPayGroup.getOrganizer().getUserId())
                .organizerName(dutchPayGroup.getOrganizer().getName())
                .paymentId(dutchPayGroup.getPaymentId())
                .groupName(dutchPayGroup.getGroupName())
                .totalAmount(dutchPayGroup.getTotalAmount())
                .participantCount(dutchPayGroup.getParticipantCount())
                .amountPerPerson(dutchPayGroup.getAmountPerPerson())
                .status(dutchPayGroup.getStatus())
                .participants(dutchPayGroup.getParticipants().stream()
                        .map(ParticipantResponse::from)
                        .collect(Collectors.toList()))
                .createdAt(dutchPayGroup.getCreatedAt())
                .updatedAt(dutchPayGroup.getUpdatedAt())
                .build();
    }
}