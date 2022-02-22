package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiMemberService {

    private final MemberService memberService;

    public Member updateMemberInfo(String email, MemberUpdateDto.Request requestDto) {
        Member member = memberService.findByEmail(email);
        member.updateMemberName(requestDto.getMemberName());
        return member;
    }

}
