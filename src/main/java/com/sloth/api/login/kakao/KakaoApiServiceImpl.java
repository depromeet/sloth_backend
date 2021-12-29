package com.sloth.api.login.kakao;

import com.sloth.domain.member.constant.SocialType;
import com.sloth.api.login.service.SocialApiSerivce;
import com.sloth.config.auth.dto.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        log.info("kakao email: {}", kakaoUserInfo.getKakaoAccount().getEmail());
        log.info("kakao UserInfo id : {}", kakaoUserInfo.getId());
        log.info("kakao nickname: {}", kakaoUserInfo.getKakaoAccount().getProfile().getNickname());

        return OAuthAttributes.builder()
                .email(StringUtils.isBlank(kakaoUserInfo.getKakaoAccount().getEmail()) ? kakaoUserInfo.getId() : kakaoUserInfo.getKakaoAccount().getEmail())
                .name(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
                .socialType(SocialType.KAKAO)
                .password(passwordEncoder.encode(pass))
                .build();
    }

}
