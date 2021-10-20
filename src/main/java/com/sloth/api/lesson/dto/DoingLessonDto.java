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
        private Boolean weeklyFinished;
        private int restDate;
        private String site;
        private String category;
        private int presentNumber;
        private int todoNumber;

        public static DoingLessonDto.Response create (Lesson lesson) {
            return Response.builder()
                    .doingLessonId(lesson.getLessonId())
                    .weeklyFinished(false) // TODO 계산 필요
                    .restDate(lesson.getRestDate())// });
                    .site(lesson.getSite().getSiteName())
                    .category(lesson.getCategory().getCategoryName())
                    .presentNumber(lesson.getPresentNumber())
                    .todoNumber(lesson.getTotalNumber() - lesson.getPresentNumber())
                    .build();
        }

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @ApiModel(value = "진행 중인 강의 요청 객체", description = "진행 중인 강의 요청 객체")
    public class Request {
        private Long memberId;
    }

}
