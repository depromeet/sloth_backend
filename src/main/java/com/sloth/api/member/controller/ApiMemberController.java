package com.sloth.api.member.controller;


import com.sloth.api.member.dto.MemberInfoDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.service.ApiMemberService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.apache.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseEntity<MemberInfoDto> getMemberInfo(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        MemberInfoDto memberInfo = memberService.getMemberInfo(token);
        return ResponseEntity.ok(memberInfo);
    }

    @PatchMapping("/{memberId}")
    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public Long update(@PathVariable Long memberId, @RequestBody MemberUpdateDto requestDto) {
        return memberService.update(memberId, requestDto);
    }

}