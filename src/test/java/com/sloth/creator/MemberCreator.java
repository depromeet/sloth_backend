package com.sloth.creator;

import com.sloth.domain.member.Member;

public class MemberCreator {

    public static Member createStubMember() {
        return Member.builder()
                .memberName("홍길동")
                .email("email@email.com")
                .build();
    }

}
