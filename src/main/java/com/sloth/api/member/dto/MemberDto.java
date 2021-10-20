package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

@Getter
@ApiModel(value = "마이페이지 조회 객체", description = "마이페이지 조회 객체")
public class MemberDto {

    private Long id;
    private String name;
    private String email;

    public MemberDto(Member entity) {
        this.id = entity.getMemberId();
        this.name = entity.getMemberName();
        this.email = entity.getEmail();
    }
}
