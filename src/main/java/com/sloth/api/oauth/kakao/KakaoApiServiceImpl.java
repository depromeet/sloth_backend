package com.sloth.api.oauth.kakao;

import com.sloth.api.oauth.dto.SocialType;
import com.sloth.api.oauth.service.SocialApiSerivce;
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
public class KakaoApiServiceImpl implements SocialApiSerivce {

    private final PasswordEncoder passwordEncoder;
    private final KakaoFeignClient kakaoFeignClient;

    @Value("${my-pass}")
    private String pass;

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        String contentType = "application/x-www-form-urlencoded;charset=utf-8";
        KakaoUserInfo kakaoUserInfo = kakaoFeignClient.kakaoLogin(contentType, "Bearer " + accessToken);
        log.info("kakao email: " + kakaoUserInfo.getKakaoAccount().getEmail());
        log.info("kakao nickname: " + kakaoUserInfo.getKakaoAccount().getProfile().getNickname());

        return OAuthAttributes.builder()
                .email(kakaoUserInfo.getKakaoAccount().getEmail())
                .name(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .socialType(SocialType.KAKAO)
                .password(passwordEncoder.encode(pass))
                .build();
    }

}
