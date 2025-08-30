package com.heyyoung.solsol.domain.user.service;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.user.dto.GetAccountResponse;
import com.heyyoung.solsol.domain.user.dto.UserDto;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 관련 비즈니스 로직 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 검색 (학번/이름)
     * @param query 검색어
     * @param limit 조회 개수 제한
     * @return 검색된 사용자 목록
     */
    public List<UserDto> searchUsers(String query, int limit) {
        if (query == null || query.trim().isEmpty()) {
            throw new SolsolException(SolsolErrorCode.INVALID_REQUEST);
        }

        if (limit <= 0 || limit > 100) {
            limit = 20; // 기본값
        }

        Pageable pageable = PageRequest.of(0, limit);
        List<User> users = userRepository.findByStudentNumberOrNameContaining(query.trim(), pageable);

        return users.stream()
                .map(UserDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자 ID로 상세 정보 조회
     * @param userId 사용자 ID
     * @return 사용자 상세 정보
     */
    public UserDto getUserById(String userId) {
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));

        return UserDto.from(user);
    }

    public GetAccountResponse  getAccountByUserId(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));

        return new GetAccountResponse(user.getAccountNo(), user.getAccountBalance().intValue());
    }
}