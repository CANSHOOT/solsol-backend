package com.heyyoung.solsol.domain.auth.controller;

import com.heyyoung.solsol.domain.auth.dto.*;
import com.heyyoung.solsol.domain.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 사용자 인증 API 컨트롤러
 * 회원가입, 로그인, 토큰 갱신 엔드포인트를 제공
 */
@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 회원가입 API
     * @param request 회원가입 요청 데이터 (이메일, 학번, 이름, 학과정보 등)
     * @return JWT 토큰이 포함된 인증 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 로그인 API
     * @param request 로그인 요청 데이터 (이메일)
     * @return JWT 토큰이 포함된 인증 응답
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * JWT 토큰 갱신 API
     * @param request Refresh Token이 포함된 요청 데이터
     * @return 새로운 JWT 토큰이 포함된 인증 응답
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(response);
    }
}