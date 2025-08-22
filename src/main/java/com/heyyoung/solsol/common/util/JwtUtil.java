package com.heyyoung.solsol.common.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 토큰 생성 및 검증을 담당하는 유틸리티 클래스
 * AccessToken에는 userId와 userKey를 포함하고,
 * RefreshToken에는 userId만 포함한다.
 */
@Component
@Slf4j
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtUtil(@Value("${jwt.secret-key}") String secretKey,
                   @Value("${jwt.access-token-validity}") long accessTokenValidity,
                   @Value("${jwt.refresh-token-validity}") long refreshTokenValidity) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenValidity = accessTokenValidity;
        this.refreshTokenValidity = refreshTokenValidity;
    }

    /**
     * AccessToken 생성
     * @param userId 사용자 ID (이메일)
     * @param userKey 외부 API 연동용 사용자 키
     * @return JWT AccessToken
     */
    public String generateAccessToken(String userId, String userKey) {
        return createToken(userId, userKey, accessTokenValidity);
    }

    /**
     * RefreshToken 생성
     * @param userId 사용자 ID (이메일)
     * @return JWT RefreshToken
     */
    public String generateRefreshToken(String userId) {
        return createToken(userId, null, refreshTokenValidity);
    }

    private String createToken(String userId, String userKey, long validity) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validity);

        JwtBuilder builder = Jwts.builder()
                .subject(userId)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key);

        if (userKey != null) {
            builder.claim("userKey", userKey);
        }

        return builder.compact();
    }

    public String getUserIdFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.getSubject();
    }

    public String getUserKeyFromToken(String token) {
        Claims claims = parseClaims(token);
        return claims.get("userKey", String.class);
    }

    /**
     * JWT 토큰 유효성 검증
     * @param token 검증할 JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT 토큰 검증 실패: {}", e.getMessage());
            return false;
        }
    }

    /**
     * JWT 토큰에서 클레임(사용자 정보) 추출
     * @param token JWT 토큰
     * @return 토큰에 포함된 클레임 정보
     */
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}