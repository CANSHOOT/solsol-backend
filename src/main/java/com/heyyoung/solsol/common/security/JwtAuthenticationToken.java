package com.heyyoung.solsol.common.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

/**
 * JWT 토큰 기반 인증 객체
 * userId와 userKey 정보를 포함
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    
    private final String userId;
    private final String userKey;
    
    public JwtAuthenticationToken(String userId, String userKey) {
        super(Collections.emptyList());
        this.userId = userId;
        this.userKey = userKey;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
    
    @Override
    public String getName() {
        return userId;
    }
    
    /**
     * 외부 API 호출용 userKey 반환
     * @return userKey
     */
    public String getUserKey() {
        return userKey;
    }
}