package com.sloth.creator;

import com.sloth.domain.member.Member;

public class MemberCreator {

    public static Member createStubMember(String email) {
        return Member.builder()
                .memberName("홍길동")
                .email(email)
                .build();
    }

    public static Member createNoEmailMember(String clientId) {
        return Member.builder()
                .memberName("홍길동")
                .email(clientId)
                .build();
    }

}
