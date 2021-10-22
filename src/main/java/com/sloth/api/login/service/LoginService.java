package com.sloth.api.login.service;

import com.sloth.api.login.dto.FormLoginRequestDto;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.app.member.service.MemberService;
import com.sloth.config.auth.TokenProvider;
import com.sloth.config.auth.dto.OAuthAttributes;
import com.sloth.config.auth.dto.TokenDto;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LoginService {

    private final TokenProvider tokenProvider;
    private final ModelMapper modelMapper;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    /**
     * OAuth 로그인
     * @param accessToken
     * @param socialType
     * @return
     */
    public ResponseJwtTokenDto login(String accessToken, SocialType socialType) {

        // 소셜 회원 정보 조회
        OAuthAttributes oAuthAttributes = getSocialUserInfo(accessToken, socialType);

        // 토큰 생성
        TokenDto tokenDto = tokenProvider.createTokenDto(oAuthAttributes.getEmail());

        // 회원가입
        memberService.saveMember(oAuthAttributes, tokenDto);

        return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
    }

    private OAuthAttributes getSocialUserInfo(String accessToken, SocialType socialType) {
        SocialApiSerivce socialApiSerivce = SocialApiServiceFactory.getSocialApiService(socialType);
        OAuthAttributes oAuthAttributes = socialApiSerivce.getUserInfo(accessToken);
        return oAuthAttributes;
    }

    /**
     * 폼 회원가입
     * @param formRequestDto
     */
    public void register(FormJoinDto formRequestDto) {
        memberService.saveMember(formRequestDto);
    }

    /**
     * 폼 로그인
     * @param formLoginRequestDto
     * @return
     */
    public ResponseJwtTokenDto login(FormLoginRequestDto formLoginRequestDto) {
        Member member = memberService.findByEmail(formLoginRequestDto.getEmail());

        if(!passwordEncoder.matches(formLoginRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidParameterException("비밀번호를 확인해주세요");
        }

        MemberToken memberToken = member.getMemberToken();
        TokenDto tokenDto = tokenProvider.createTokenDto(formLoginRequestDto.getEmail());

        if (memberToken == null) {
            memberService.saveRefreshToken(member, tokenDto);
        } else {
            LocalDateTime refreshTokenExpirationTime = memberToken.getTokenExpirationTime();
            if(!tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {
                // 리프레시 토큰 만료 시간 갱신
                memberToken.updateRefreshTokenExpireTime(memberToken.getRefreshToken());
                tokenDto.setRefreshToken(memberToken.getRefreshToken());
            } else if(tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {   //refresh token이 만료 됐을 경우
                memberService.saveRefreshToken(member, tokenDto);
            }
        }

        return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
    }

}
