package com.heyyoung.solsol.domain.dutchpay.repository;

import com.heyyoung.solsol.domain.dutchpay.entity.DutchPayParticipant;
import com.heyyoung.solsol.domain.dutchpay.entity.ParticipantPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * 더치페이 참여자 리포지토리
 */
public interface DutchPayParticipantRepository extends JpaRepository<DutchPayParticipant, Long> {

    /**
     * 그룹 ID와 사용자 ID로 참여자 조회
     * @param groupId 그룹 ID
     * @param userId 사용자 ID
     * @return 참여자 정보
     */
    @Query("SELECT p FROM DutchPayParticipant p WHERE p.dutchPayGroup.groupId = :groupId AND p.user.userId = :userId")
    Optional<DutchPayParticipant> findByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") String userId);

    /**
     * 사용자 ID로 참여자 목록 조회 (더치페이 그룹 정보 포함)
     * @param userId 사용자 ID
     * @return 참여자 목록
     */
    @Query("SELECT p FROM DutchPayParticipant p JOIN FETCH p.dutchPayGroup dpg JOIN FETCH dpg.organizer WHERE p.user.userId = :userId ORDER BY p.joinedAt DESC")
    List<DutchPayParticipant> findByUserIdWithGroup(@Param("userId") String userId);

    /**
     * 그룹 ID로 모든 참여자 조회
     * @param groupId 그룹 ID
     * @return 참여자 목록
     */
    @Query("SELECT p FROM DutchPayParticipant p JOIN FETCH p.user WHERE p.dutchPayGroup.groupId = :groupId")
    List<DutchPayParticipant> findByGroupIdWithUser(@Param("groupId") Long groupId);

    /**
     * 그룹 ID와 결제 상태로 참여자 수 조회
     * @param groupId 그룹 ID
     * @param status 결제 상태
     * @return 참여자 수
     */
    @Query("SELECT COUNT(p) FROM DutchPayParticipant p WHERE p.dutchPayGroup.groupId = :groupId AND p.paymentStatus = :status")
    Long countByGroupIdAndPaymentStatus(@Param("groupId") Long groupId, @Param("status") ParticipantPaymentStatus status);

    /**
     * 그룹 ID로 참여자가 존재하는지 확인
     * @param groupId 그룹 ID
     * @param userId 사용자 ID
     * @return 존재 여부
     */
    @Query("SELECT COUNT(p) > 0 FROM DutchPayParticipant p WHERE p.dutchPayGroup.groupId = :groupId AND p.user.userId = :userId")
    boolean existsByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") String userId);

    // 내가 보낼 돈: 참가자 테이블에서 내 userId로 조회
    List<DutchPayParticipant> findByUser_UserId(String userId);

    // 특정 그룹의 참가자들
    List<DutchPayParticipant> findByDutchPayGroup_GroupId(Long groupId);

    Optional<DutchPayParticipant> findByDutchPayGroup_GroupIdAndUser_UserId(Long groupId, String userId);
}