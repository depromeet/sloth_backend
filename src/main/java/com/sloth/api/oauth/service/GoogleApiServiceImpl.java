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
public class GoogleApiServiceImpl implements SocialApiSerivce {

    private final GoogleFeignClient googleFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${my-pass}")
    private String pass;

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {

        GoogleUserInfo googleUserInfo = googleFeignClient.googleLogin("Bearer" + accessToken);
        log.info("email: "+googleUserInfo.getEmail());
        log.info("name: "+googleUserInfo.getName());

        return OAuthAttributes.builder()
                .email(googleUserInfo.getEmail())
                .name(googleUserInfo.getName())
                .socialType(SocialType.GOOGLE)
                .password(passwordEncoder.encode(pass))
                .build();
    }
}