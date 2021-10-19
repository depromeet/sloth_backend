package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LessonNumberDto {

    @Data
    @Builder
    @ApiModel(value = "강의 갯수 반환 객체", description = "강의 갯수 반환 객체")
    public static class Response {
        private Long id;
        private Integer presentNumber;
        private Boolean isFinished;
        private Boolean weeklyFinished;

        public static LessonNumberDto.Response create(Lesson lesson) {
            return LessonNumberDto.Response.builder()
                    .id(lesson.getId())
                    .presentNumber(lesson.getPresentNumber())
                    .isFinished(lesson.getIsFinished())
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "강의 갯수 요청 객체", description = "강의 갯수 요청 객체")
    public static class Request {

        private Long id;
        private int count;
    }

}
