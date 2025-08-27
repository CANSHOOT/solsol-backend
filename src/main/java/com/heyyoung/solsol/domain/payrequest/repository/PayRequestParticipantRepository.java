package com.heyyoung.solsol.domain.payrequest.repository;

import com.heyyoung.solsol.domain.payrequest.entity.PayParticipantStatus;
import com.heyyoung.solsol.domain.payrequest.entity.PayRequestParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayRequestParticipantRepository extends JpaRepository<PayRequestParticipant, Long> {
    // 내가 받은 요청들(= 내가 참가자인 항목들)
    List<PayRequestParticipant> findByUser_UserIdOrderByCreatedAtDesc(Long userId);

    // 특정 요청의 참가자들
    List<PayRequestParticipant> findByPayRequest_PayRequestId(Long payRequestId);

    // 특정 요청에서 특정 사용자 1건
    Optional<PayRequestParticipant> findByPayRequest_PayRequestIdAndUser_UserId(Long payRequestId, Long userId);

    // 결제 대기/완료 필터
    List<PayRequestParticipant> findByPayRequest_PayRequestIdAndStatus(Long payRequestId, PayParticipantStatus status);
}
