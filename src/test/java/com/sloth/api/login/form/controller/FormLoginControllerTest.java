package com.sloth.api.login.form.controller;

import com.google.gson.Gson;
import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.api.login.form.dto.FormJoinDto;
import com.sloth.api.login.form.dto.FormLoginRequestDto;
import com.sloth.api.login.form.service.FormLoginService;
import com.sloth.creator.MemberCreator;
import com.sloth.domain.member.Member;
import com.sloth.global.config.auth.TokenProvider;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.exception.NeedEmailConfirmException;
import com.sloth.global.exception.handler.GlobalExceptionHandler;
import com.sloth.global.util.DateTimeUtils;
import com.sloth.test.base.BaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class FormLoginControllerTest extends BaseApiController {

    @InjectMocks
    private FormLoginController formLoginController;

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private FormLoginService formLoginService;

    private MockMvc mockMvc;

    private String testPassword = "testPassword";

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(formLoginController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }
    @Test
    @DisplayName("폼 회원가입")
    void form_register() throws Exception {
        // given
        String name = "name";
        String email = "email@email.com";
        FormJoinDto request = new FormJoinDto(name, email, testPassword, testPassword);
        Member member = MemberCreator.createStubMember(email);

        doReturn(member).when(formLoginService).register(any(FormJoinDto.class));


        // when
        ResultActions result = mockMvc.perform(post("/api/form/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("success"));
    }

    @Test
    @DisplayName("폼 로그인 - 이메일 인증 안함")
    void form_login_notEmailConfirm() throws Exception {
        //given
        String password = "password";
        String email = "email@email.com";
        FormLoginRequestDto requestDto = new FormLoginRequestDto(email, password);
        String errorMessage = "이메일 인증을 하지 않은 사용자입니다. 이메일로 보낸 코드를 확인하세요.";
        doThrow(new NeedEmailConfirmException(errorMessage))
                .when(formLoginService).loginForm(any(FormLoginRequestDto.class));
        //when
        ResultActions result = mockMvc.perform(post("/api/form/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(requestDto)));

        //then
        result.andExpect(status().isForbidden())
                .andExpect(jsonPath("$.code").value(HttpStatus.FORBIDDEN.value()))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                .andExpect(jsonPath("$.referedUrl").value("/api/form/login"));
    }

    @Test
    @DisplayName("폼 로그인 - 비밀번호 불일치")
    void form_login_wrong_password() throws Exception {
        //given
        String password = "wrongPassword";
        String email = "email@email.com";
        FormLoginRequestDto requestDto = new FormLoginRequestDto(email, password);
        String errorMessage = "비밀번호를 확인해주세요.";
        doThrow(new InvalidParameterException(errorMessage))
                .when(formLoginService).loginForm(any(FormLoginRequestDto.class));
        //when
        ResultActions result = mockMvc.perform(post("/api/form/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(requestDto)));

        //then
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorMessage").value(errorMessage))
                .andExpect(jsonPath("$.referedUrl").value("/api/form/login"));
    }

    @Test
    @DisplayName("폼 로그인")
    void form_login() throws Exception {
        //given
        FormLoginRequestDto requestDto = new FormLoginRequestDto("email@email.com", "password");
        final Date time = DateTimeUtils.createDate(2020, 1, 1);

        ResponseJwtTokenDto response = ResponseJwtTokenDto.builder()
                .accessToken(accessToken)
                .accessTokenExpireTime(time)
                .refreshToken(accessToken)
                .refreshTokenExpireTime(time)
                .isNewMember(false)
                .build();

        when(formLoginService.loginForm(any(FormLoginRequestDto.class))).thenReturn(response);

        //when
        ResultActions result = mockMvc.perform(post("/api/form/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new Gson().toJson(requestDto)));

        //then
        final String timeString = DateTimeUtils.convertToString(time);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken))
                .andExpect(jsonPath("$.accessTokenExpireTime").value(timeString))
                .andExpect(jsonPath("$.refreshToken").value(accessToken))
                .andExpect(jsonPath("$.refreshTokenExpireTime").value(timeString))
                .andExpect(jsonPath("$.isNewMember").value(false));
    }
}