package com.heyyoung.solsol.domain.settlement.service;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.settlement.dto.FeeStatusResponse;
import com.heyyoung.solsol.domain.settlement.entity.CouncilFee;
import com.heyyoung.solsol.domain.settlement.repository.CouncilFeeRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeeStatusService {

    private final CouncilFeeRepository councilFeeRepository;

    @PersistenceContext
    private EntityManager em;

    /**
     * 특정 학생회(councilId)의 특정 회비(feeId)에 대한 전체 납부 현황
     */
    public FeeStatusResponse getFeeStatusByCouncil(Long councilId, Long feeId) {
        // (0) fee가 해당 학생회 소속인지 검증
        CouncilFee fee = councilFeeRepository.findById(feeId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.PAYMENT_NOT_FOUND));
        if (!Objects.equals(fee.getCouncilId(), councilId)) {
            throw new SolsolException(SolsolErrorCode.INVALID_REQUEST);
        }

        // (1) 학생회 소속 전체 사용자 조회(삭제되지 않은 사용자만)
        TypedQuery<User> uq = em.createQuery(
                "select u from User u " +
                        "where u.deletedAt is null and u.councilId = :councilId " +
                        "order by u.name asc", User.class);
        uq.setParameter("councilId", councilId);
        var users = uq.getResultList();

        if (users.isEmpty()) return FeeStatusResponse.empty();

        // (2) 해당 fee 납부 완료자 userId와 latest paidAt을 한 번에 조회
        var userIds = users.stream().map(User::getUserId).toList();

        TypedQuery<Object[]> pq = em.createQuery(
                "select p.userId, max(p.paidAt) " +
                        "from CouncilFeePayment p " +
                        "where p.feeId = :feeId " +
                        "  and p.paymentStatus = com.heyyoung.solsol.domain.settlement.entity.FeePaymentStatus.COMPLETED " +
                        "  and p.userId in :userIds " +
                        "group by p.userId", Object[].class);
        pq.setParameter("feeId", feeId);
        pq.setParameter("userIds", userIds);

        var paidRows = pq.getResultList();
        Map<String, Instant> paidMap = new HashMap<>();
        for (Object[] row : paidRows) {
            paidMap.put((String) row[0], (Instant) row[1]);
        }

        // (3) 매핑
        var list = users.stream()
                .map(u -> new FeeStatusResponse.StudentStatus(
                        u.getUserId(),
                        u.getName(),
                        u.getDepartment().getDepartmentName(),
                        u.getStudentNumber(),
                        paidMap.containsKey(u.getUserId()),
                        paidMap.get(u.getUserId())
                ))
                .collect(Collectors.toList());

        return new FeeStatusResponse(users.size(), paidMap.size(), list);
    }
}
