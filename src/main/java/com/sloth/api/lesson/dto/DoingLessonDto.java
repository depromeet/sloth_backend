package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class DoingLessonDto {

    @Data
    @Builder
    @ApiModel(value = "진행 중인 강의 반환 객체", description = "진행 중인 강의 반환 객체")
    public static class Response {
        private Long doingLessonId;
        private String lessonName;
        private Boolean weeklyFinished;
        private int restDate;
        private String siteName;
        private String categoryName;
        private int presentNumber;
        private int todoNumber;

        public static DoingLessonDto.Response create (Lesson lesson) {
            return Response.builder()
                    .lessonName(lesson.getLessonName())
                    .doingLessonId(lesson.getLessonId())
                    .weeklyFinished(false) // TODO 계산 필요
                    .restDate(lesson.getRestDate())
                    .siteName(lesson.getSite().getSiteName())
                    .categoryName(lesson.getCategory().getCategoryName())
                    .presentNumber(lesson.getPresentNumber())
                    .todoNumber(lesson.getTotalNumber() - lesson.getPresentNumber())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "진행 중인 강의 요청 객체", description = "진행 중인 강의 요청 객체")
    public static class Request {
        private Long memberId;
    }

}
