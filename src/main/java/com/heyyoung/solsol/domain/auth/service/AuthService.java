package com.heyyoung.solsol.domain.auth.service;

import com.heyyoung.solsol.common.util.JwtUtil;
import com.heyyoung.solsol.domain.auth.dto.*;
import com.heyyoung.solsol.domain.department.entity.Department;
import com.heyyoung.solsol.domain.department.repository.DepartmentRepository;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import com.heyyoung.solsol.external.dto.ApiKeyReissueResponse;
import com.heyyoung.solsol.external.dto.UserCreateResponse;
import com.heyyoung.solsol.external.service.FinOpenApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 인증 비즈니스 로직을 처리하는 서비스
 * 회원가입, 로그인, JWT 토큰 갱신 기능을 제공
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final FinOpenApiService finOpenApiService;
    private final JwtUtil jwtUtil;

    /**
     * 회원가입 처리
     * 1. 학번 중복 검사
     * 2. 외부 API로 API 키 재발급
     * 3. 외부 API로 사용자 계정 생성
     * 4. 내부 DB에 사용자 정보 저장
     * 5. JWT 토큰 생성 및 반환
     */
    public AuthResponse signup(SignupRequest request) {
        // 1. 학번 중복 체크
        if (userRepository.existsByStudentNumber(request.getStudentNumber())) {
            throw new IllegalArgumentException("이미 존재하는 학번입니다.");
        }

        // 2. API Key 재발급
        ApiKeyReissueResponse apiKeyResponse = finOpenApiService.reissueApiKey(request.getEmail());
        if (apiKeyResponse == null || apiKeyResponse.getApiKey() == null) {
            throw new RuntimeException("API Key 재발급에 실패했습니다.");
        }

        // 3. 외부 API로 사용자 계정 생성
        UserCreateResponse userCreateResponse = finOpenApiService.createUser(
                apiKeyResponse.getApiKey(), request.getEmail());
        if (userCreateResponse == null) {
            throw new RuntimeException("사용자 계정 생성에 실패했습니다.");
        }

        // 4. Department 조회
        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학과입니다."));
        }

        // 5. User 엔티티 생성 및 저장
        User user = User.builder()
                .userId(request.getEmail())
                .userKey(userCreateResponse.getUserKey())
                .studentNumber(request.getStudentNumber())
                .name(request.getName())
                .department(department)
                .councilId(request.getCouncilId())
                .isCouncilOfficer(request.getIsCouncilOfficer())
                .build();

        userRepository.save(user);

        // 6. JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getUserKey());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }

    /**
     * 로그인 처리
     * 이미 가입된 사용자의 이메일로 로그인 처리
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1. 사용자 존재 여부 확인
        User user = userRepository.findByUserIdAndDeletedAtIsNull(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. JWT 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getUserKey());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }

    /**
     * JWT 토큰 갱신
     * Refresh Token을 사용하여 새로운 Access Token과 Refresh Token 발급
     */
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        // 1. Refresh Token 검증
        if (!jwtUtil.validateToken(request.getRefreshToken())) {
            throw new IllegalArgumentException("유효하지 않은 Refresh Token입니다.");
        }

        // 2. 사용자 정보 조회
        String userId = jwtUtil.getUserIdFromToken(request.getRefreshToken());
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 3. 새로운 토큰 생성
        String newAccessToken = jwtUtil.generateAccessToken(user.getUserId(), user.getUserKey());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        return AuthResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(user.getUserId())
                .name(user.getName())
                .build();
    }
}