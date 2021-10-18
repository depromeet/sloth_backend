package com.sloth.api.member.dto;

import lombok.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Getter
@NoArgsConstructor
public class MemberUpdateDto {

    private String name;

    @Builder
    public MemberUpdateDto(String name) {
        this.name = name;
    }

}
