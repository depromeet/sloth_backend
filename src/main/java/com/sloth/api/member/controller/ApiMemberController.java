package com.sloth.api.member.controller;

import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.global.dto.ApiResult;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.resolver.CurrentEmail;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/member")
public class ApiMemberController {

    private final ApiMemberService apiMemberService;

    @GetMapping
    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<MemberInfoDto> getMemberInfo(@CurrentEmail String email) {
        MemberInfoDto memberInfoDto = apiMemberService.findMemberInfoDto(email);
        return ResponseEntity.ok(memberInfoDto);
    }

    @Deprecated
    @PatchMapping
    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity updateMemberInfo(@CurrentEmail String email, @Valid @RequestBody MemberUpdateDto.Request requestDto,
                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        MemberUpdateDto.Response memberUpdateResponseDto = apiMemberService.updateMemberInfo(email, null, requestDto);
        return ResponseEntity.ok(memberUpdateResponseDto);
    }

    @PatchMapping(value = "/v2")
    @Operation(summary = "회원 정보 수정 API V2", description = "회원 정보 수정 API V2")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<MemberUpdateDto.Response> updateMemberInfoV2(@CurrentEmail String email,
                                             @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                             @Valid @RequestPart("requestDto") MemberUpdateDto.Request requestDto,
                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        MemberUpdateDto.Response memberUpdateResponseDto = apiMemberService.updateMemberInfo(email, profileImage, requestDto);

        return ResponseEntity.ok(memberUpdateResponseDto);
    }

    @PatchMapping("/delete")
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity deleteMember(@CurrentEmail String email) {

        log.info("member delete start");
        log.info("email : {}", email);

        apiMemberService.deleteMember(email);

        ApiResult result = ApiResult.createOk();
        log.info("member delete end");

        return ResponseEntity.ok(result);
    }
}