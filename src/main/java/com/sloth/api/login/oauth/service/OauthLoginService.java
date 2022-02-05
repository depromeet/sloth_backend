package com.sloth.api.login.oauth.service;

import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.api.login.oauth.social.service.SocialApiSerivce;
import com.sloth.api.login.oauth.social.service.SocialApiServiceFactory;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.service.MemberService;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.config.auth.dto.OAuthAttributes;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.global.util.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OauthLoginService {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ModelMapper modelMapper;

    /**
     * OAuth 로그인
     * @param accessToken
     * @param socialType
     * @return
     */
    public ResponseJwtTokenDto loginOauth(String accessToken, SocialType socialType) {

        // 소셜 회원 정보 조회
        OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, socialType);
        log.info("oAuthAttributes : {}", oAuthAttributes.toString());

        // 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());
        log.info("tokenDto : {}", tokenDto.toString());

        // 회원가입
        Boolean isNewMember = false;
        Optional<Member> optionalMember = memberService.getOptionalMember(oAuthAttributes.getEmail());
        if (optionalMember.isEmpty()) { // 기존 회원이 아닐 때
            Member oauthMember = memberService.createOauthMember(oAuthAttributes);
            memberService.saveMember(oauthMember, tokenDto);
            isNewMember = true;
        } else memberService.saveRefreshToken(optionalMember.get(), tokenDto); // 기존 회원일 때

        ResponseJwtTokenDto responseJwtTokenDto = modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
        responseJwtTokenDto.setIsNewMember(isNewMember);

        return responseJwtTokenDto;
    }

    private OAuthAttributes getSocialUserInfo(String accessToken, SocialType socialType) {
        SocialApiSerivce socialApiSerivce = SocialApiServiceFactory.getSocialApiService(socialType);
        OAuthAttributes oAuthAttributes = socialApiSerivce.getUserInfo(accessToken);
        return oAuthAttributes;
    }
}
