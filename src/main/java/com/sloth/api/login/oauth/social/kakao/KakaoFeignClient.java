package com.sloth.api.login.oauth.social.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@FeignClient(url = "https://kapi.kakao.com/v2/user/me", name = "KakaoClient")
public interface KakaoFeignClient {

    @PostMapping(value = "", produces = "application/json", consumes = "application/json")
    KakaoUserInfo kakaoLogin(
            @RequestHeader("Content-type") String contentType,
            @RequestHeader("Authorization") String accessToken
    );
}
