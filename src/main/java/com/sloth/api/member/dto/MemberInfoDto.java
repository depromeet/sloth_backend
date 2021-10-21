package com.sloth.api.member.dto;

import com.sloth.domain.member.Member;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@ApiModel(value = "회원 상세 정보 객체", description = "회원 상세 정보 객체")
public class MemberInfoDto {

    private Long memberId;
    private String memberName;
    private String email;

    public MemberInfoDto(Member entity) {
        this.memberId = entity.getMemberId();
        this.memberName = entity.getMemberName();
        this.email = entity.getEmail();
    }
}
