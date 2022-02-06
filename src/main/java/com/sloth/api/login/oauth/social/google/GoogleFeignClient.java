package com.sloth.api.login.oauth.social.google;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@FeignClient(url = "https://www.googleapis.com/oauth2/v2/userinfo", name = "GoogleClient")
public interface GoogleFeignClient {

    @GetMapping(value = "/", produces = "application/json", consumes = "application/json")
    GoogleUserInfo googleLogin(
            @RequestHeader("Authorization") String accessToken
    );
}
