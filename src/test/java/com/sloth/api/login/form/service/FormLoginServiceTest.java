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
import com.sloth.domain.memberToken.constant.TokenRefreshCritnTime;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.config.auth.dto.TokenDto;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.exception.NeedEmailConfirmException;
import com.sloth.global.util.DateTimeUtils;
import com.sloth.global.util.MailService;
import org.joda.time.field.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
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
    @DisplayName("?????? ??????")
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
    @DisplayName("?????? ?????? - ???????????? ?????????")
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
        assertEquals("??????????????? ??????????????????.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("?????? ?????? - ????????? ?????? ?????????")
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
        assertEquals("????????? ????????? ?????? ?????? ??????????????????. ???????????? ?????? ????????? ???????????????.",
                needEmailConfirmException.getMessage());
    }

    @Test
    @DisplayName("??? ????????? - ??? ????????? (?????? ??????)") //TODO ???????????? mock ?????? ?????? ??????
    void login_form_first() {
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(stubMember.getMemberToken()).willReturn(null);
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        doNothing().when(formLoginService).verifyMember(requestDto, stubMember);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberService, times(1)).saveRefreshToken(stubMember, tokenDto);
        verify(formLoginService, times(1)).verifyMember(requestDto, stubMember);
        assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
    }

    @Test
    @DisplayName("??? ????????? - ????????? (?????? ?????? ???)")
    void login_form_is_not_token_expired() { //TODO ???????????? mock ?????? ?????? ??????
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();
        final LocalDateTime tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(
                DateTimeUtils.createDate(2020, 1, 1));
        final MemberToken memberToken = Mockito.spy(MemberToken.createMemberToken(stubMember, "abcabcabc", tokenExpirationTime));

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(true);
        given(stubMember.isEmailConfirm()).willReturn(true);
        given(stubMember.getMemberToken()).willReturn(memberToken);
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        given(tokenProvider.isTokenExpired(memberToken.getTokenExpirationTime())).willReturn(false);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberToken, times(1)).updateRefreshTokenExpireTime(any(), eq(TokenRefreshCritnTime.HOURS_72));
        assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
        assertEquals(memberToken.getRefreshToken(), response.getRefreshToken());
    }

    @Test
    @DisplayName("??? ????????? - ????????? (?????? ??????)")
    void login_form_is_token_expired() { //TODO ???????????? mock ?????? ?????? ??????
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Member stubMember = Mockito.spy(MemberCreator.createEmailPasswordMember(requestDto.getEmail(), "password"));
        final TokenDto tokenDto = createTokenDto();
        final LocalDateTime tokenExpirationTime = DateTimeUtils.convertToLocalDateTime(
                DateTimeUtils.createDate(2020, 1, 1));
        final MemberToken memberToken = Mockito.spy(MemberToken.createMemberToken(stubMember, "abcabcabc", tokenExpirationTime));

        given(memberService.findByEmail(requestDto.getEmail())).willReturn(stubMember);
        given(passwordEncoder.matches(requestDto.getPassword(), stubMember.getPassword())).willReturn(true);
        given(stubMember.isEmailConfirm()).willReturn(true);
        given(stubMember.getMemberToken()).willReturn(memberToken);
        given(tokenProvider.createTokenDto(stubMember.getEmail())).willReturn(tokenDto);
        given(tokenProvider.isTokenExpired(memberToken.getTokenExpirationTime())).willReturn(true);

        //when
        final ResponseJwtTokenDto response = formLoginService.loginForm(requestDto);

        //then
        verify(memberService, times(1)).saveRefreshToken(stubMember, tokenDto);
        assertEquals(modelMapper.map(tokenDto, ResponseJwtTokenDto.class), response);
        assertEquals(memberToken.getRefreshToken(), response.getRefreshToken());
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
    @DisplayName("????????? ?????? ?????? ??????")
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
    @DisplayName("????????? ?????? - ????????? ????????????")
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
        assertEquals("????????? ??????????????????.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? - ????????? ????????????")
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
        assertEquals("?????? ????????? ?????? ????????????.", invalidParameterException.getMessage());
    }

    @Test
    @DisplayName("????????? ??????")
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
    @DisplayName("????????? ?????? ?????? ????????????")
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