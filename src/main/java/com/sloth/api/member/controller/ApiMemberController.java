package com.sloth.api.member.controller;


import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.global.exception.InvalidParameterException;
import com.sloth.global.resolver.CurrentEmail;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PatchMapping("")
    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity updateMemberInfo(@CurrentEmail String email, @Valid @RequestBody MemberUpdateDto.Request requestDto,
                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        Member member = apiMemberService.updateMemberInfo(email, requestDto);

        MemberUpdateDto.Response responseMemberUpdateDto = MemberUpdateDto.Response.builder()
                .memberName(member.getMemberName())
                .build();

        return ResponseEntity.ok(responseMemberUpdateDto);
    }

}