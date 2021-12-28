package com.sloth.api.member.service;

import com.sloth.api.member.dto.MemberUpdateDto;
import com.sloth.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiMemberService {

    public Member updateMemberInfo(Member member, MemberUpdateDto.Request requestDto) {
        member.updateMemberName(requestDto.getMemberName());
        return member;
    }

}
