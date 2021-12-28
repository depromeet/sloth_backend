package com.sloth.api.member.controller;


import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.domain.member.Member;
import com.sloth.exception.InvalidParameterException;
import com.sloth.resolver.CurrentMember;
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

    private final ApiMemberService memberService;

    @GetMapping("")
    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<MemberInfoDto> getMemberInfo(@CurrentMember Member member) {
        return ResponseEntity.ok(new MemberInfoDto(member));
    }

    @PatchMapping("")
    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity updateMemberInfo(@CurrentMember Member member, @Valid @RequestBody MemberUpdateDto.Request requestDto,
                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            InvalidParameterException.throwErrorMessage(bindingResult);
        }

        member = memberService.updateMemberInfo(member, requestDto);

        MemberUpdateDto.Response responseMemberUpdateDto = MemberUpdateDto.Response.builder()
                .memberName(member.getMemberName())
                .build();

        return ResponseEntity.ok(responseMemberUpdateDto);
    }

}