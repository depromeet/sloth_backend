package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class DoingLessonDto {

    @Data
    @Builder
    @ApiModel(value = "진행 중인 강의 반환 객체", description = "진행 중인 강의 반환 객체")
    public static class Response {
        @ApiModelProperty(value = "강의 아이디")
        private Long lessonId;

        @ApiModelProperty(value = "강의명")
        private String lessonName;

        @ApiModelProperty(value = "이번주 끝남 여부")
        private Boolean weeklyFinished;

        @ApiModelProperty(value = "강의 목표 남은일수")
        private int remainDay;

        @ApiModelProperty(value = "사이트명")
        private String siteName;

        @ApiModelProperty(value = "카테고리명")
        private String categoryName;

        @ApiModelProperty(value = "완강한 강의 개수")
        private int presentNumber;

        @ApiModelProperty(value = "남은 강의 개수")
        private int remainNumber;

        public static DoingLessonDto.Response create (Lesson lesson) {
            return Response.builder()
                    .lessonName(lesson.getLessonName())
                    .lessonId(lesson.getLessonId())
                    .weeklyFinished(false) // TODO 계산 필요
                    .remainDay(lesson.getRemainDay())
                    .siteName(lesson.getSite().getSiteName())
                    .categoryName(lesson.getCategory().getCategoryName())
                    .presentNumber(lesson.getPresentNumber())
                    .remainNumber(lesson.getTotalNumber() - lesson.getPresentNumber())
                    .build();
        }

    }

}
