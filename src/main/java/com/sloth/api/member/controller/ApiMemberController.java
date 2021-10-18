package com.sloth.api.member.controller;


import com.sloth.api.lesson.dto.RequestLessonDto;
import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.api.member.dto.ResponseMemberDto;
import com.sloth.api.member.service.ApiMemberService;
import com.sloth.exception.InvalidParameterException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/members")
public class ApiMemberController {

    private final ApiMemberService memberService;

    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API")
    @GetMapping("/{memberId}")
    public ResponseMemberDto findById (@PathVariable Long memberId) {
        return memberService.findById(memberId);
    }

    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API")
    @PatchMapping("/{memberId}")
    public Long update(@PathVariable Long memberId, @RequestBody MemberUpdateDto requestDto) {
        return memberService.update(memberId, requestDto);
    }


}