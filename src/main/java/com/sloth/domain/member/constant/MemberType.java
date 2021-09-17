package com.sloth.domain.member.constant;

public enum MemberType {

    KAKAO("20");

    private final String memberType;

    MemberType(String memberType){
        this.memberType=memberType;
    }

    public String getMemberType() {
        return memberType;
    }

}
