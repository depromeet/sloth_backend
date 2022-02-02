package com.sloth.api.login.form.service;

import com.sloth.api.login.dto.*;
import com.sloth.api.login.form.dto.EmailConfirmRequestDto;
import com.sloth.api.login.form.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.form.dto.FormJoinDto;
import com.sloth.api.login.form.dto.FormLoginRequestDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.exception.NeedEmailConfirmException;
import com.sloth.global.util.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FormLoginService {

    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final ModelMapper modelMapper;

    /**
     * 폼 회원가입
     * @param formRequestDto
     */
    public Member register(FormJoinDto formRequestDto) {
        return memberService.saveMember(formRequestDto);
    }

    /**
     * 폼 로그인
     * @param formLoginRequestDto
     * @return
     */
    public ResponseJwtTokenDto loginForm(FormLoginRequestDto formLoginRequestDto) {
        Member member = memberService.findByEmail(formLoginRequestDto.getEmail());

        if(!passwordEncoder.matches(formLoginRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidParameterException("비밀번호를 확인해주세요");
        }

        if (!member.isEmailConfirm()) {
            throw new NeedEmailConfirmException("이메일 인증을 하지 않은 사용자입니다. 이메일로 보낸 코드를 확인하세요.");
        }

        MemberToken memberToken = member.getMemberToken();
        TokenDto tokenDto = tokenProvider.createTokenDto(formLoginRequestDto.getEmail());

        if (memberToken == null) {
            memberService.saveRefreshToken(member, tokenDto);
        } else {
            LocalDateTime refreshTokenExpirationTime = memberToken.getTokenExpirationTime();
            if(!tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {
                // 리프레시 토큰 만료 시간 갱신
                memberToken.updateRefreshTokenExpireTime(LocalDateTime.now(), TokenRefreshCritnTime.HOURS_72);
                tokenDto.setRefreshToken(memberToken.getRefreshToken());
            } else if(tokenProvider.isTokenExpired(refreshTokenExpirationTime)) {   //refresh token이 만료 됐을 경우
                memberService.saveRefreshToken(member, tokenDto);
            }
        }

        return modelMapper.map(tokenDto, ResponseJwtTokenDto.class);
    }

    public void sendConfirmEmail(Member member) throws MessagingException {
        String body = "<html><body><h1> 나나공 이메일 인증 </h1>" +
                "<div>나나공 서비스를 이용하시려면 앱에서 아래 번호를 인증해주세요.</div>" +
                "<div><b>"+ member.getEmailConfirmCode() +"</b></div>" +
                "</body></html>";
        String to = member.getEmail();
        String subject = "나나공 서비스 인증 이메일";

        mailService.sendEmail(to, subject, body);
    }

    public void confirmEmail(EmailConfirmRequestDto emailConfirmRequestDto) {
        Member member = memberService.findByEmail(emailConfirmRequestDto.getEmail());
        if (!member.confirmEmail(emailConfirmRequestDto.getEmailConfirmCode())) {
            throw new InvalidParameterException("인증에 실패했습니다.");
        }
        if (!passwordEncoder.matches(emailConfirmRequestDto.getPassword(), member.getPassword())) {
            throw new InvalidParameterException("회원 정보가 옳지 않습니다.");
        }
        member.activate();
    }

    public Member updateConfirmEmailCode(EmailConfirmResendRequestDto requestDto) {
        return memberService.updateConfirmEmailCode(requestDto);
    }
}
