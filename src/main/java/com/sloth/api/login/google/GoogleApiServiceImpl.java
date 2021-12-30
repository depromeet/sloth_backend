package com.sloth.api.login.google;

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
public class GoogleApiServiceImpl implements SocialApiSerivce {

    private final GoogleFeignClient googleFeignClient;
    private final PasswordEncoder passwordEncoder;

    @Value("${my-pass}")
    private String pass;

    @Override
    public OAuthAttributes getUserInfo(String accessToken) {
        accessToken = "Bearer " + accessToken.replace("Bearer", "").trim();
        log.info("accessToken : {}", accessToken);

        GoogleUserInfo googleUserInfo = googleFeignClient.googleLogin(accessToken);
        log.info("email : {}", googleUserInfo.getEmail());
        log.info("name : {}", googleUserInfo.getName());

        return OAuthAttributes.builder()
                .email(StringUtils.isBlank(googleUserInfo.getEmail()) ? googleUserInfo.getId() : googleUserInfo.getEmail())
                .name(googleUserInfo.getName())
                .socialType(SocialType.GOOGLE)
                .password(passwordEncoder.encode(pass))
                .build();
    }
}
