package com.heyyoung.solsol.domain.payrequest.repository;

import com.heyyoung.solsol.domain.payrequest.entity.PayRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PayRequestRepository extends JpaRepository<PayRequest, Long> {
    // 내가 보낸 요청들
    List<PayRequest> findByRequester_UserIdOrderByCreatedAtDesc(Long requesterId);

    // groupId(더치페이)로 묶음 조회
    List<PayRequest> findByDutchPayGroup_GroupIdOrderByCreatedAtDesc(Long groupId);
}
