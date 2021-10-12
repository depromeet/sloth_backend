package com.sloth.api.oauth.service;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.sloth.api.oauth.GoogleFeignClient;
import com.sloth.api.oauth.dto.*;
import com.sloth.app.member.service.MemberService;
import com.sloth.config.auth.TokenProvider;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final TokenProvider tokenProvider;
    private final GoogleFeignClient googleFeignClient;
    private final ModelMapper modelMapper;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Value("${my-pass}")
    private String pass;

    public ResponseJwtTokenDto login(OauthRequestDto oauthRequestDto) {

        String accessToken = oauthRequestDto.getAccessToken();
        SocialType socialType = oauthRequestDto.getSocialType();

        OAuthAttributes oAuthAttributes = null;

        if(socialType.equals(SocialType.GOOGLE)) {

            GoogleUserInfo googleUserInfo = googleFeignClient.googleLogin("Bearer" + accessToken);
            log.info("email: "+googleUserInfo.getEmail());
            log.info("name: "+googleUserInfo.getName());

            oAuthAttributes = OAuthAttributes.builder()
                    .email(googleUserInfo.getEmail())
                    .name(googleUserInfo.getName())
                    .socialType(SocialType.GOOGLE)
                    .password(passwordEncoder.encode(pass))
                    .build();
        }
        TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());

        // 회원가입
        Member member = memberService.saveMember(oAuthAttributes, tokenDto);

        return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
    }

}
