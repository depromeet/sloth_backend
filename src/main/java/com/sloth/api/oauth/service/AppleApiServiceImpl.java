package com.sloth.api.oauth.service;

import com.sloth.api.oauth.GoogleFeignClient;
import com.sloth.api.oauth.dto.GoogleUserInfo;
import com.sloth.api.oauth.dto.SocialType;
import com.sloth.config.auth.dto.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AppleApiServiceImpl implements SocialApiSerivce {

    private final GoogleFeignClient googleFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${my-pass}")
    private String pass;

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        return OAuthAttributes.builder()
                .build();
    }
}
