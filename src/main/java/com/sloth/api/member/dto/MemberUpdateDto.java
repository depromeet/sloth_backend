package com.sloth.api.member.dto;

import lombok.*;
import io.swagger.annotations.ApiModel;

@Getter
@NoArgsConstructor
@ApiModel(value = "회원 정보(이름) 변경 객체", description = "회원 정보(이름) 변경 객체")
public class MemberUpdateDto {

    private String memberName;

    @Builder
    public MemberUpdateDto(String memberName) {
        this.memberName = memberName;
    }

}
