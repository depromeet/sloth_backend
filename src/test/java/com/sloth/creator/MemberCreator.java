package com.sloth.creator;

import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.memberToken.MemberToken;
import com.sloth.domain.memberToken.constant.MemberTokenType;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class MemberCreator {

    public static Member createStubMember(String email) {
        return Member.builder()
                .memberName("홍길동")
                .email(email)
                .role(Role.USER)
                .memberTokens(new ArrayList<>())
                .build();
    }

    public static Member createNoEmailMember(String clientId) {
        return Member.builder()
                .memberName("홍길동")
                .email(clientId)
                .memberTokens(new ArrayList<>())
                .build();
    }

    public static Member createMember(Long memberId, String email) {
        Member member = Member.builder()
                .memberId(memberId)
                .password("password")
                .memberName("홍길동")
                .email(email)
                .lessons(new ArrayList<>())
                .role(Role.USER)
                .memberTokens(new ArrayList<>())
                .build();

        LocalDateTime tokenExpireTime = LocalDateTime.of(2022, 1, 1, 12, 0);
        MemberToken memberToken = new MemberToken(1L, "previous-refresh", tokenExpireTime, member, MemberTokenType.LOGIN_REFRESH);
        member.getMemberTokens().add(memberToken);

        return member;
    }

    public static Member createEmailPasswordMember(String email, String password) {
        return Member.builder()
                .memberName("홍길동")
                .email(email)
                .password(password)
                .memberTokens(new ArrayList<>())
                .build();
    }
}
