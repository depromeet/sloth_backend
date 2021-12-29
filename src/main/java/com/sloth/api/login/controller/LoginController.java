package com.sloth.api.login.controller;

import com.sloth.api.dto.ApiResult;
import com.sloth.api.login.dto.*;
import com.sloth.api.login.validator.FormLoginValidator;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.api.login.service.LoginService;
import com.sloth.api.login.validator.FormRegisterValidator;
import com.sloth.exception.InvalidParameterException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;

    @PostMapping(value = "/oauth/login" , headers = { "Content-type=application/json" }, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "OAuth 로그인 API", description = "OAuth Access 토큰으로 로그인 시 JWT 토큰 반환, 현재 socialType은 GOOGLE, KAKAO만 구현 완료")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="access token", dataType = "string", value = "access token", required = true, paramType = "header")
    })
    // TODO param 정보 입력
    //@ApiImplicitParam(name = "acceessToken", dataType = "body", required = true, dataTypeClass = com.sloth.api.oauth.dto.OauthRequestDto.class),
    //@ApiImplicitParam(name = "socialType", dataType = "body", required = true, dataTypeClass = com.sloth.api.oauth.dto.OauthRequestDto.class)
    public ResponseEntity<ResponseJwtTokenDto> login(@RequestBody OauthRequestDto oauthRequestDto, HttpServletRequest httpServletRequest) {

        log.info("oauth login start");

        String accessToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("토큰값을 입력해주세요");
        }

        if(!SocialType.isSocialType(oauthRequestDto.getSocialType()) || oauthRequestDto.getSocialType().equals(SocialType.FORM.name())) {
            throw new InvalidParameterException("잘못된 소셜 타입입니다. 'GOOGLE', 'KAKAO', 'APPLE' 중에 입력해주세요");
        }

        SocialType socialType = SocialType.from(oauthRequestDto.getSocialType());
        ResponseJwtTokenDto responseJwtTokenDto = loginService.login(accessToken, socialType);

        log.info("oauth login end");

        return ResponseEntity.ok(responseJwtTokenDto);
    }

    @PostMapping(value = "/form/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Form 회원가입 API", description = "폼 회원가입 API")
    public ResponseEntity<ApiResult> register(@Valid @RequestBody FormJoinDto formRequestDto, Errors errors) throws MessagingException {
        new FormRegisterValidator().validate(formRequestDto, errors);

        if(errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }

        Member savedMember = loginService.register(formRequestDto);
        loginService.sendConfirmEmail(savedMember);

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
        ResponseJwtTokenDto responseJwtTokenDto = loginService.login(formRequestDto);

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
        loginService.confirmEmail(emailConfirmRequestDto);
        return ResponseEntity.ok(ApiResult.createOk());
    }

    @PostMapping("/email-confirm-resend")
    @Operation(summary = "이메일 검증 재전송 API", description = "이메일 검증 재전송 API")
    public ResponseEntity<ApiResult> confirmEmailResend(@Valid @RequestBody EmailConfirmResendRequestDto requestDto, Errors errors) throws MessagingException {
        if (errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }
        Member member = loginService.updateConfirmEmailCode(requestDto);
        loginService.sendConfirmEmail(member);
        return ResponseEntity.ok(ApiResult.createOk());
    }

}
