package com.sloth.api.login.controller;

import com.sloth.api.dto.ApiResult;
import com.sloth.api.login.dto.FormJoinDto;
import com.sloth.api.login.dto.FormLoginRequestDto;
import com.sloth.api.login.dto.OauthRequestDto;
import com.sloth.api.login.dto.ResponseJwtTokenDto;
import com.sloth.api.login.validator.FormLoginValidator;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        String accessToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isBlank(accessToken)) {
            throw new InvalidParameterException("토큰값을 입력해주세요");
        }

        if(!SocialType.isSocialType(oauthRequestDto.getSocialType()) || oauthRequestDto.getSocialType().equals(SocialType.FORM.name())) {
            throw new InvalidParameterException("잘못된 소셜 타입입니다. 'GOOGLE', 'KAKAO', 'APPLE' 중에 입력해주세요");
        }

        SocialType socialType = SocialType.from(oauthRequestDto.getSocialType());
        ResponseJwtTokenDto responseJwtTokenDto = loginService.login(accessToken, socialType);

        return ResponseEntity.ok(responseJwtTokenDto);
    }

    @PostMapping(value = "/form/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Form 회원가입 API", description = "폼 회원가입 API")
    public ResponseEntity<ApiResult> register(@Valid @RequestBody FormJoinDto formRequestDto, Errors errors) {
        new FormRegisterValidator().validate(formRequestDto, errors);

        if(errors.hasErrors()) {
            InvalidParameterException.throwErrorMessage(errors);
        }

        loginService.register(formRequestDto);

        // TODO 이메일 인증 추가

        ApiResult response = ApiResult.builder()
                .code(String.valueOf(HttpStatus.OK.value()))
                .message("success")
                .build();

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

}
