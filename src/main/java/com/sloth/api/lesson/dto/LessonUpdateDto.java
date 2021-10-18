package com.sloth.api.lesson.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LessonUpdateDto {

    @Getter @Setter
    @ApiModel(value = "강의 업데이트 요청 객체", description = "강의 업데이트 요청 객체")
    public static class Request {

        @ApiModelProperty(value = "강의명")
        @NotBlank(message = "강의명을 입력해 주세요")
        private String lessonName;

        @ApiModelProperty(value = "강의 총개수")
        @NotNull(message = "강의 총개수를 입력해 주세요")
        private Integer totalNumber;

        @ApiModelProperty(value = "카테고리 아이디")
        @NotNull(message = "카테고리 아이디를 입력해 주세요")
        private Long categoryId;

        @ApiModelProperty(value = "사이트 아이디")
        @NotNull(message = "사이트 아이디를 입력해 주세요")
        private Long siteId;

    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @ApiModel(value = "강의 업데이트 반환 객체", description = "강의 업데이트 반환 객체")
    public static class Response {

        @ApiModelProperty(value = "강의 아이디")
        private Long lessonId;

        @ApiModelProperty(value = "강의명")
        private String lessonName;

        @ApiModelProperty(value = "강의 총개수")
        private Integer totalNumber;

        @ApiModelProperty(value = "카테고리 아이디")
        private Long categoryId;

        @ApiModelProperty(value = "사이트 아이디")
        private Long siteId;
    }

}
