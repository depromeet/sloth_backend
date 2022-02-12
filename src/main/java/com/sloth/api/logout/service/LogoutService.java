package com.sloth.api.logout.service;

import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.service.MemberTokenService;
import com.sloth.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class LogoutService {

    private final MemberService memberService;
    private final MemberTokenService memberTokenService;

    public void logout(String email, LocalDateTime now) {
        Member member = memberService.findByEmail(email);
        MemberToken memberToken = memberTokenService.findMemberTokenByMemberId(member.getMemberId());
        memberToken.expireToken(now);
    }

}
