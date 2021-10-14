package com.sloth.api.oauth.service;

import com.sloth.api.oauth.dto.OauthRequestDto;
import com.sloth.api.oauth.dto.ResponseJwtTokenDto;
import com.sloth.api.oauth.dto.SocialType;
import com.sloth.app.member.service.MemberService;
import com.sloth.config.auth.TokenProvider;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MissingServletRequestParameterException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final MemberService memberService;

    public ResponseJwtTokenDto login(String accessToken, OauthRequestDto oauthRequestDto) throws MissingServletRequestParameterException {

        // 소셜 회원 정보 조회
        OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, oauthRequestDto);

        // 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());

        // 회원가입
        Member member = memberService.saveMember(oAuthAttributes, tokenDto);

        return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
    }

    private OAuthAttributes getSocialUserInfo(String accessToken, OauthRequestDto oauthRequestDto) {
        SocialType socialType = oauthRequestDto.getSocialType();
        SocialApiSerivce socialApiSerivce = SocialApiServiceFactory.getSocialApiService(socialType);
        OAuthAttributes oAuthAttributes = socialApiSerivce.getUserInfo(accessToken);
        return oAuthAttributes;
    }

}
