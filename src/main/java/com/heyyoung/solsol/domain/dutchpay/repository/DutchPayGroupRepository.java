package com.heyyoung.solsol.domain.dutchpay.repository;

import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayGroup;
import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 더치페이 그룹 리포지토리
 */
public interface DutchPayGroupRepository extends JpaRepository<DutchPayGroup, Long> {

    /**
     * 더치페이 그룹 ID로 조회 (참여자 정보 포함)
     * @param groupId 그룹 ID
     * @return 더치페이 그룹 정보
     */
    @Query("SELECT dpg FROM DutchPayGroup dpg LEFT JOIN FETCH dpg.participants p LEFT JOIN FETCH p.user WHERE dpg.groupId = :groupId")
    Optional<DutchPayGroup> findByIdWithParticipants(@Param("groupId") Long groupId);

    /**
     * 주최자 ID로 더치페이 그룹 목록 조회
     * @param organizerId 주최자 ID
     * @param pageable 페이징 정보
     * @return 더치페이 그룹 목록
     */
    @Query("SELECT dpg FROM DutchPayGroup dpg WHERE dpg.organizer.userId = :organizerId ORDER BY dpg.createdAt DESC")
    List<DutchPayGroup> findByOrganizerUserId(@Param("organizerId") String organizerId, Pageable pageable);

    /**
     * 상태별 더치페이 그룹 조회
     * @param status 더치페이 상태
     * @return 더치페이 그룹 목록
     */
    List<DutchPayGroup> findByStatusOrderByCreatedAtDesc(DutchPayStatus status);

    /**
     * 결제 ID로 더치페이 그룹 조회
     * @param paymentId 결제 ID
     * @return 더치페이 그룹 정보
     */
    Optional<DutchPayGroup> findByPaymentId(Long paymentId);
}