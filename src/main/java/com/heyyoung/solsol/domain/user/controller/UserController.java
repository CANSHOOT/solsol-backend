package com.heyyoung.solsol.domain.user.controller;

import com.heyyoung.solsol.domain.user.dto.UserDto;
import com.heyyoung.solsol.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 사용자 관련 API 컨트롤러
 */
@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
}