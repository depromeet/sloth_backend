package com.sloth.api.login.apple;

import com.sloth.api.login.google.GoogleUserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@FeignClient(url = "https://appleid.apple.com/auth", name = "AppleClient")
public interface AppleFeignClient {

    @GetMapping(value = "/", produces = "application/json", consumes = "application/json")
    AppleUserInfo appleLogin(
            @RequestHeader("Authorization") String accessToken
    );
}
