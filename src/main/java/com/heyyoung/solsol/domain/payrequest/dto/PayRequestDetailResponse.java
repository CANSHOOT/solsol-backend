package com.heyyoung.solsol.domain.payrequest.dto;

import com.heyyoung.solsol.domain.payrequest.entity.PayParticipantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PayRequestDetailResponse {
    private Long payRequestId;
    private String requesterId;
    private Long groupId;
    private String memo;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private List<Row> participants;

    @Getter
    @AllArgsConstructor
    public static class Row {
        private Long participantId;
        private String userId;
        private BigDecimal amount;
        private PayParticipantStatus status;
        private String transferUniqueNo; // 결제 성공 시 세팅
    }
}
