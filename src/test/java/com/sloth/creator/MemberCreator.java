package com.sloth.creator;

import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;

import java.util.ArrayList;

public class MemberCreator {

    public static Member createStubMember(String email) {
        return Member.builder()
                .memberName("홍길동")
                .email(email)
                .role(Role.USER)
                .build();
    }

    public static Member createNoEmailMember(String clientId) {
        return Member.builder()
                .memberName("홍길동")
                .email(clientId)
                .build();
    }

    public static Member createMember(Long memberId, String email) {
        return Member.builder()
                .memberId(memberId)
                .password("password")
                .memberName("홍길동")
                .email(email)
                .lessons(new ArrayList<>())
                .role(Role.USER)
                .build();
    }

    public static Member createEmailPasswordMember(String email, String password) {
        return Member.builder()
                .memberName("홍길동")
                .email(email)
                .password(password)
                .build();
    }
}
