package com.sloth.api.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

public class MemberUpdateDto {

    @Getter @Setter
    @ApiModel(value = "회원 정보(이름) 변경 객체", description = "회원 정보(이름) 변경 객체")
    @EqualsAndHashCode
    public static class Request {

        @ApiModelProperty(value = "회원 이름")
        @NotNull(message = "회원 이름을 입력해주세요")
        private String memberName;

    }


    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @ApiModel(value = "회원 정보(이름) 변경 반환 객체", description = "회원 정보(이름) 변경 반환 객체")
    public static class Response {

        @ApiModelProperty(value = "회원 이름")
        private String memberName;

    }

}
