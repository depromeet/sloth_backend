package com.sloth.api.member.dto;

import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Getter
@NoArgsConstructor
@ApiModel(value = "회원 정보(이름) 변경 객체", description = "회원 정보(이름) 변경 객체")
public class MemberUpdateDto {

    private String name;

    @Builder
    public MemberUpdateDto(String name) {
        this.name = name;
    }

}
