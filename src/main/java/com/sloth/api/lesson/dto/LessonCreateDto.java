package com.sloth.api.lesson.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.time.LocalDate;

public class LessonCreateDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @Builder
    @ApiModel(value = "강의 생성 반환 객체",description = "강의 생성 반환 객체")
    public static class Response {
        private Long lessonId;
    }

    @Getter
    @NoArgsConstructor
    @ApiModel(value = "강의 생성 요청 객체", description = "강의 생성 요청 객체")
    public static class Request {

        @ApiModelProperty(value = "강의명")
        private String lessonName;

        @ApiModelProperty(value = "강의 시작일")
        private LocalDate startDate;

        @ApiModelProperty(value = "강의 목표 종료일")
        private LocalDate endDate;

        @ApiModelProperty(value = "강의 가격")
        private int price;

        @ApiModelProperty(value = "강의 알림일")
        private String alertDays;

        @ApiModelProperty(value = "강의 개수")
        private int totalNumber;

        @ApiModelProperty(value = "강의 생성하는 멤버 아이디")
        private Long memberId;

        @ApiModelProperty(value = "사이트 아이디")
        private Long siteId;

        @ApiModelProperty(value = "카테고리 아이디")
        private Long categoryId;

    }
}
