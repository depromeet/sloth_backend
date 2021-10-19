package com.sloth.api.member.controller;


import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.dto.MemberDto;
import com.sloth.api.member.service.ApiMemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class ApiMemberController {

    private final ApiMemberService memberService;

    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API")
    @GetMapping("/{memberId}")
    public MemberDto findById (@PathVariable Long memberId) {
        return memberService.findById(memberId);
    }

    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    @PatchMapping("/{memberId}")
    public Long update(@PathVariable Long memberId, @RequestBody MemberUpdateDto requestDto) {
        return memberService.update(memberId, requestDto);
    }


}