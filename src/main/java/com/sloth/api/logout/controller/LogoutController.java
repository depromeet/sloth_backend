package com.sloth.api.logout.controller;

import com.sloth.api.logout.service.LogoutService;
import com.sloth.global.config.auth.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LogoutController {

    private final TokenProvider tokenProvider;
    private final LogoutService logoutService;

    @PostMapping(value = "/logout")
    @Operation(summary = "로그아웃", description = "로그아웃 처리 시 db에 저장된 refresh token 만료 처리")
    public ResponseEntity<String> logout(@RequestHeader(value="Authorization") String accessToken, String fcmToken) {
        String email  = tokenProvider.getEmail(accessToken);
        logoutService.logout(email, LocalDateTime.now(), fcmToken);
        return ResponseEntity.ok().body("logout success");
    }

}
