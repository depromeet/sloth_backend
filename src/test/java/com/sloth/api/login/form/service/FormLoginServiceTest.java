package com.sloth.api.login.form.service;

import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.api.login.form.dto.EmailConfirmRequestDto;
import com.sloth.api.login.form.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.form.dto.FormJoinDto;
import com.sloth.api.login.form.dto.FormLoginRequestDto;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.constant.MemberTokenType;
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import com.sloth.domain.memberToken.exception.MemberTokenNotFoundException;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.global.exception.ErrorMessage;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.exception.NeedEmailConfirmException;
import com.sloth.global.util.DateTimeUtils;
import com.sloth.global.util.MailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FormLoginServiceTest {

    @Spy
    @InjectMocks
    FormLoginService formLoginService;

    @Mock private MemberService memberService;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private MailService mailService;

    @Mock private TokenProvider tokenProvider;
    @Spy private ModelMapper modelMapper;

    @Test
    @DisplayName("회원 가입")
    void register() {
        // given
        FormJoinDto formJoinDto = new FormJoinDto("name", "email@email.com", "password", "password");
        final Member newMember = MemberCreator.createEmailPasswordMember(formJoinDto.getEmail(), formJoinDto.getPassword());
        given(memberService.saveMember(formJoinDto)).willReturn(newMember);

        // when
        final Member savedMember = formLoginService.register(formJoinDto);

        //then
        assertEquals(formJoinDto.getEmail(), savedMember.getEmail());
        assertEquals(formJoinDto.getPassword(), savedMember.getPassword());
    }

    @Test
    @DisplayName("멤버 인증 - 비밀번호 불일치")
    void verify_member_not_match_password() {
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "wrongPassword");
        final Member stubMember = MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password");

        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(false);

        //when
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> {
            formLoginService.verifyMember(requestDto, stubMember);
        });

        //then
        assertEquals("비밀번호를 확인해주세요.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("멤버 인증 - 이메일 인증 미완료")
    void verify_member_need_email_confirm() {
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));

        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(true);
        given(stubMember.isEmailConfirm()).willReturn(false);

        //when
        final NeedEmailConfirmException needEmailConfirmException = assertThrows(NeedEmailConfirmException.class, () -> {
            formLoginService.verifyMember(requestDto, stubMember);
        });

        //then
        assertEquals("이메일 인증을 하지 않은 사용자입니다. 이메일로 보낸 코드를 확인하세요.",
                needEmailConfirmException.getMessage());
    }

    @Test
    @DisplayName("폼 로그인 - 첫 로그인 (토큰 없음)") //TODO 불필요한 mock 처리 체크 필요
    void login_form_first() {
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(stubMember.getLoginRefreshToken()).willThrow(new MemberTokenNotFoundException(ErrorMessage.REFRESH_TOKEN_NOT_FOUND.getMessage()));
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        doNothing().when(formLoginService).verifyMember(requestDto, stubMember);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberService, times(1)).saveRefreshToken(stubMember, tokenDto);
        verify(formLoginService, times(1)).verifyMember(requestDto, stubMember);
        //assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
    }

    @Test
    @DisplayName("폼 로그인 - 로그인 (토큰 만료 전)")
    void login_form_is_not_token_expired() { //TODO 불필요한 mock 처리 체크 필요
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();
        final LocalDateTime tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(
                DateTimeUtils.createDate(2020, 1, 1));
        final MemberToken memberToken = Mockito.spy(MemberToken.createMemberToken(stubMember, "abcabcabc", tokenExpirationTime, MemberTokenType.LOGIN_REFRESH));

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(true);
        given(stubMember.isEmailConfirm()).willReturn(true);
        given(stubMember.getLoginRefreshToken()).willReturn(memberToken);
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        given(tokenProvider.isTokenExpired(memberToken.getTokenExpirationTime())).willReturn(false);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberToken, times(1)).updateRefreshTokenExpireTime(any(), eq(TokenRefreshCritnTime.HOURS_72));
        assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
        assertEquals(memberToken.getToken(), response.getRefreshToken());
    }

    @Test
    @DisplayName("폼 로그인 - 로그인 (토큰 만료)")
    void login_form_is_token_expired() { //TODO 불필요한 mock 처리 체크 필요
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();
        final LocalDateTime tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(
                DateTimeUtils.createDate(2020, 1, 1));
        final MemberToken memberToken = Mockito.spy(MemberToken.createMemberToken(stubMember, "abcabcabc", tokenExpirationTime, MemberTokenType.LOGIN_REFRESH));

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(true);
        given(stubMember.isEmailConfirm()).willReturn(true);
        given(stubMember.getLoginRefreshToken()).willReturn(memberToken);
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        given(tokenProvider.isTokenExpired(memberToken.getTokenExpirationTime())).willReturn(true);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberService, times(1)).saveRefreshToken(stubMember, tokenDto);
        assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
        assertEquals(memberToken.getToken(), response.getRefreshToken());
    }

    private TokenDto createTokenDto() {
        return TokenDto.builder()
                .grantType("bearer")
                .accessToken("abcabc")
                .accessTokenExpireTime(DateTimeUtils.createDate(2020,1,1))
                .refreshToken("abcabcabc")
                .refreshTokenExpireTime(DateTimeUtils.createDate(2020,1,1))
                .build();
    }

    @Test
    @DisplayName("이메일 인증 코드 발송")
    void send_confirm_email() throws MessagingException {
        //given
        final Member stubMember = MemberCreator.createStubMember("email@email.com");
        stubMember.updateConfirmEmailCode("122133", LocalDateTime.of(2020, 1, 1, 0, 0));
        doNothing().when(mailService).sendEmail(eq(stubMember.getEmail()), any(), any());

        //when
        formLoginService.sendConfirmEmail(stubMember);

        //then
        verify(mailService, times(1)).sendEmail(eq(stubMember.getEmail()), any(), any());
    }

    @Test
    @DisplayName("이메일 인증 - 잘못된 인증코드")
    void confirm_email_wrong_code() {
        //given
        final String email = "email@email.com";
        final String password = "password";
        final Member member = MemberCreator.createEmailPasswordMember(email, password);

        member.updateConfirmEmailCode("111111", LocalDateTime.of(2020, 1, 1, 0, 0));
        EmailConfirmRequestDto requestDto = new EmailConfirmRequestDto(email, password, "123123");

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(member);

        //when
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> {
            formLoginService.confirmEmail(requestDto);
        });

        //then
        assertEquals("인증에 실패했습니다.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("이메일 인증 - 잘못된 패스워드")
    void confirm_email_wrong_password() {
        //given
        final String email = "email@email.com";
        final Member member = MemberCreator.createEmailPasswordMember(email, "password");

        final String emailConfirmCode = "111111";
        member.updateConfirmEmailCode(emailConfirmCode, LocalDateTime.of(2020, 1, 1, 0, 0));
        EmailConfirmRequestDto requestDto = new EmailConfirmRequestDto(email, "wrongPassword", emailConfirmCode);

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(member);
        given(passwordEncoder.matches(requestDto.getPassword(), member.getPassword())).willReturn(false);

        //when
        final InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> {
            formLoginService.confirmEmail(requestDto);
        });

        //then
        assertEquals("회원 정보가 옳지 않습니다.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("이메일 인증")
    void confirm_email() {
        //given
        final String email = "email@email.com";
        final String password = "password";
        final Member member = Mockito.spy(MemberCreator.createEmailPasswordMember(email, password));

        final String emailConfirmCode = "111111";
        member.updateConfirmEmailCode(emailConfirmCode, LocalDateTime.of(2020, 1, 1, 0, 0));
        EmailConfirmRequestDto requestDto = new EmailConfirmRequestDto(email, password, emailConfirmCode);

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(member);
        given(passwordEncoder.matches(requestDto.getPassword(), member.getPassword())).willReturn(true);

        //when
        formLoginService.confirmEmail(requestDto);

        //then
        verify(member, times(1)).activate();
    }

    @Test
    @DisplayName("이메일 인증 코드 업데이트")
    void update_confirm_email_code() {
        //given
        EmailConfirmResendRequestDto requestDto = new EmailConfirmResendRequestDto("email@email.com","password");
        final Member updatedMember = MemberCreator.createEmailPasswordMember(requestDto.getEmail(), requestDto.getPassword());
        given(memberService.updateConfirmEmailCode(requestDto)).willReturn(updatedMember);

        //when
        final Member member = formLoginService.updateConfirmEmailCode(requestDto);

        //then
        assertEquals(requestDto.getEmail(), updatedMember.getEmail());
        assertEquals(requestDto.getPassword(), updatedMember.getPassword());
    }
}