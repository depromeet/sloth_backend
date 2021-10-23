package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


public class LessonDetailDto {

    @Data
    @Builder
    @ApiModel(value = "강의 상세정보 반환 객체", description = "강의 상세정보 반환 객체")
    public static class Response {
        @ApiModelProperty(value = "강의 아이디")
        private Long lessonId;

        @ApiModelProperty(value = "강의 목표 남은일수")
        private int remainDay;

        @ApiModelProperty(value = "카테고리명")
        private String categoryName;

        @ApiModelProperty(value = "사이트명")
        private String siteName;

        @ApiModelProperty(value = "강의명")
        private String lessonName;

        @ApiModelProperty(value = "강의 가격")
        private Integer price;

        @ApiModelProperty(value = "강의 현재 진도율")
        private int currentProgressRate;

        @ApiModelProperty(value = "강의 목표 진도율")
        private int goalProgressRate;

        @ApiModelProperty(value = "강의 시작일")
        private LocalDate startDate;

        @ApiModelProperty(value = "강의 목표 종료일")
        private LocalDate endDate;

        @ApiModelProperty(value = "강의 개수")
        private int totalNumber;

        @ApiModelProperty(value = "강의 완강 성공 여부")
        private Boolean isFinished;

        @ApiModelProperty(value = "완강한 강의 개수")
        private int presentNumber;

        @ApiModelProperty(value = "강의 알림일")
        private String alertDays;

        @ApiModelProperty(value = "각오 메세지")
        private String message;

        @ApiModelProperty(value = "낭비한 금액")
        private int wastePrice; // TODO 날린돈 계산

        public static LessonDetailDto.Response create(Lesson lesson) {
            return Response.builder()
                    .lessonId(lesson.getLessonId())
                    .isFinished(lesson.getIsFinished())
                    .remainDay(lesson.getRemainDay())
                    .lessonName(lesson.getLessonName())
                    .categoryName(lesson.getCategory().getCategoryName())
                    .siteName(lesson.getSite().getSiteName())
                    .currentProgressRate(lesson.getCurrentProgressRate())
                    .goalProgressRate(lesson.getGoalProgressRate())
                    .totalNumber(lesson.getTotalNumber())
                    .presentNumber(lesson.getPresentNumber())
                    .startDate(lesson.getStartDate())
                    .endDate(lesson.getEndDate())
                    .price(lesson.getPrice())
                    .alertDays(lesson.getAlertDays())
                    .message(lesson.getMessage())
                    .wastePrice(40000) // TODO 계산하기
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "강의 상세정보 요청 객체", description = "강의 상세정보 요청 객체")
    public static class Request {
        private Long lessonId;
    }


}
