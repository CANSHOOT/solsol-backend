package com.heyyoung.solsol.domain.user.repository;

import com.heyyoung.solsol.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUserId(Long userId);

    Optional<User> findByUserIdAndDeletedAtIsNull(String userId);
    
    boolean existsByStudentNumber(String studentNumber);

    /**
     * 학번 또는 이름으로 사용자 검색 (삭제되지 않은 사용자만)
     * @param query 검색어
     * @param pageable 페이징 정보
     * @return 검색된 사용자 목록
     */
    @Query("SELECT u FROM User u WHERE u.deletedAt IS NULL AND " +
           "(u.studentNumber LIKE %:query% OR u.name LIKE %:query%) " +
           "ORDER BY u.name ASC")
    List<User> findByStudentNumberOrNameContaining(@Param("query") String query, Pageable pageable);
}