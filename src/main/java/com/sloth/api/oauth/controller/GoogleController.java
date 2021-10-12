package com.sloth.api.oauth.controller;

import com.sloth.api.oauth.GoogleFeignClient;
import com.sloth.api.oauth.dto.GoogleUserInfo;
import com.sloth.api.oauth.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class GoogleController {
    private final GoogleFeignClient googleFeignClient;

    @GetMapping("/google-login/{access_token}")
    public ResponseEntity<UserInfo> testFeign(@PathVariable String access_token) {
        GoogleUserInfo googleUserInfo = googleFeignClient.googleLogin("Bearer" + access_token);
        log.info("email: "+googleUserInfo.getEmail());
        log.info("name: "+googleUserInfo.getName());
        log.info("picture: "+googleUserInfo.getPicture());
        UserInfo userInfo = UserInfo.builder()
                .email(googleUserInfo.getEmail())
                .name(googleUserInfo.getName())
                .picture(googleUserInfo.getPicture())
                .build();
        return ResponseEntity.ok(userInfo);
    }
}
