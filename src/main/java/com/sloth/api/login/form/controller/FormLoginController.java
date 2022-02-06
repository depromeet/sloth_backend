package com.sloth.api.login.form.controller;

import com.sloth.api.login.dto.*;
import com.sloth.api.login.form.dto.EmailConfirmRequestDto;
import com.sloth.api.login.form.dto.EmailConfirmResendRequestDto;
import com.sloth.api.login.form.dto.FormJoinDto;
import com.sloth.api.login.form.dto.FormLoginRequestDto;
import com.sloth.api.login.form.service.FormLoginService;
import com.sloth.api.login.form.validator.FormLoginValidator;
import com.sloth.api.login.form.validator.FormRegisterValidator;
import com.sloth.domain.member.Member;
import com.sloth.global.dto.ApiResult;
import com.sloth.global.exception.InvalidParameterException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FormLoginController {

    private final FormLoginService formLoginService;

    @PostMapping(value = "/form/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Form 회원가입 API", description = "폼 회원가입 API")
    public ResponseEntity<ApiResult> register(@Valid @RequestBody FormJoinDto formRequestDto, Errors errors) throws MessagingException {
        new FormRegisterValidator().validate(formRequestDto, errors);

        if(errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }

        Member savedMember = formLoginService.register(formRequestDto);
        formLoginService.sendConfirmEmail(savedMember);

        ApiResult response = ApiResult.createOk();

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/form/login", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Form 로그인 API", description = "폼 로그인 시 JWT 토큰 반환")
    public ResponseEntity<ResponseJwtTokenDto> login(@Valid @RequestBody FormLoginRequestDto formRequestDto, Errors errors) {
        new FormLoginValidator().validate(formRequestDto, errors);

        if(errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }
        ResponseJwtTokenDto responseJwtTokenDto = formLoginService.loginForm(formRequestDto);

        return ResponseEntity.ok(responseJwtTokenDto);
    }

    // TODO ID 찾기
    // TODO 비밀번호 찾기
    // TODO 이메일로 로그인하기

    @PostMapping("/email-confirm")
    @Operation(summary = "이메일 검증 API", description = "이메일 검증 API")
    public ResponseEntity<ApiResult> confirmEmail(@Valid @RequestBody EmailConfirmRequestDto emailConfirmRequestDto, Errors errors) {
        if (errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }
        formLoginService.confirmEmail(emailConfirmRequestDto);
        return ResponseEntity.ok(ApiResult.createOk());
    }

    @PostMapping("/email-confirm-resend")
    @Operation(summary = "이메일 검증 재전송 API", description = "이메일 검증 재전송 API")
    public ResponseEntity<ApiResult> confirmEmailResend(@Valid @RequestBody EmailConfirmResendRequestDto requestDto, Errors errors) throws MessagingException {
        if (errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }
        Member member = formLoginService.updateConfirmEmailCode(requestDto);
        formLoginService.sendConfirmEmail(member);
        return ResponseEntity.ok(ApiResult.createOk());
    }
}
