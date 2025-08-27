package com.heyyoung.solsol.domain.payrequest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class PayParticipantCommand {

    @Getter
    @NoArgsConstructor
    public static class PayRequest {
        private Long participantId;     // pay_request_participants PK
        private Long payerUserId;       // 결제자 본인 (검증용)
        private String idempotencyKey;  // 중복 결제 방지 키(옵션, 없으면 내부에서 생성 예정)

        private String fromAccountNo;   // 결제자 출금 계좌(또는 핀테크번호)
        private String toAccountNo;     // 요청자 입금 계좌(또는 핀테크번호) – 없으면 서버에서 조회
        private String memo;            // 이체 메모(옵션)
    }
}
