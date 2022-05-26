package com.sloth.api.lesson.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

public class FinishedLessonDto {

    @Getter @Setter
    @EqualsAndHashCode
    @ApiModel(value = "강의 완료 확인 요청 객체", description = "강의 완료 확인 요청 객체")
    public static class Request {


        @ApiModelProperty(value = "강의 완료 여부")
        @NotNull(message = "강의 완료 여부를 입력해주세요")
        private Boolean isFinished;
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @ApiModel(value = "강의 완료 확인 반환 객체", description = "강의 완료 확인 반환 객체")
    public static class Response {

        @ApiModelProperty(value = "강의 완료 여부")
        private Boolean isFinished;


    }

}

