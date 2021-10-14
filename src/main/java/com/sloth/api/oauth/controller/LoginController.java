package com.sloth.api.oauth.controller;

import com.sloth.api.oauth.dto.OauthRequestDto;
import com.sloth.api.oauth.dto.ResponseJwtTokenDto;
import com.sloth.api.oauth.service.LoginService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LoginController {

    private final LoginService loginService;


    @Operation(summary = "Login API", description = "OAuth Access 토큰으로 로그인 시 JWT 토큰 반환, 현재 socialType은 GOOGLE만 구현 완료")
    @PostMapping(value = "/oauth/login" , headers = { "Content-type=application/json" }, produces = MediaType.APPLICATION_JSON_VALUE)
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "acceessToken", dataType = "body", required = true, dataTypeClass = com.sloth.api.oauth.dto.OauthRequestDto.class),
//            @ApiImplicitParam(name = "socialType", dataType = "body", required = true, dataTypeClass = com.sloth.api.oauth.dto.OauthRequestDto.class)
//    })
    // TODO param 정보 입력
    public ResponseEntity<ResponseJwtTokenDto> login( @RequestHeader(value = "Authorization") String accessToken,
                                                      @Valid @RequestBody OauthRequestDto oauthRequestDto) throws MissingServletRequestParameterException {

        ResponseJwtTokenDto responseJwtTokenDto = loginService.login(accessToken, oauthRequestDto);
        return ResponseEntity.ok(responseJwtTokenDto);
    }
}
