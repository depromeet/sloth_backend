package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel(value = "마이페이지 조회 객체", description = "마이페이지 조회 객체")
public class MemberDto {

    private Long memberId;
    private String memberName;
    private String email;

    public MemberDto(Member entity) {
        this.memberId = entity.getMemberId();
        this.memberName = entity.getMemberName();
        this.email = entity.getEmail();
    }
}
