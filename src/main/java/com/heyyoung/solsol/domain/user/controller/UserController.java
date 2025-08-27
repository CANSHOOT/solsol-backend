package com.heyyoung.solsol.domain.user.controller;

import com.heyyoung.solsol.common.exception.app.SolsolErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import com.heyyoung.solsol.domain.user.dto.UpdateFcmTokenRequest;
import com.heyyoung.solsol.domain.user.dto.UserDto;
import com.heyyoung.solsol.domain.user.entity.User;
import com.heyyoung.solsol.domain.user.repository.UserRepository;
import com.heyyoung.solsol.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    /**
     * 사용자 검색 (학번/이름)
     * @param query 검색어 (학번 또는 이름)
     * @param limit 조회 개수 제한
     * @return 검색된 사용자 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "20") int limit
    ) {
        List<UserDto> users = userService.searchUsers(query, limit);
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자 상세 정보 조회
     * @param userId 사용자 ID
     * @return 사용자 상세 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    /**
     * FCM 토큰 업데이트
     * @param request FCM 토큰 업데이트 요청
     * @param auth 인증 정보
     * @return 성공 응답
     */
    @PostMapping("/fcm-token")
    public ResponseEntity<String> updateFcmToken(
            @RequestBody UpdateFcmTokenRequest request,
            Authentication auth
    ) {
        String userId = auth.getName();
        User user = userRepository.findByUserIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new SolsolException(SolsolErrorCode.USER_NOT_FOUND));

        user.updateFcmToken(request.getFcmToken());
        userRepository.save(user);

        log.info("FCM 토큰 업데이트 완료 - UserId: {}", userId);
        return ResponseEntity.ok("FCM 토큰이 업데이트되었습니다.");
    }
}