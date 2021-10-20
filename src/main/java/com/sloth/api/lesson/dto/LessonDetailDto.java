package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
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
        private Long lessonDetailId;
        private Boolean isFinished;
        private int restDate;
        private String name;
        private String category;
        private String site;
        private String presentProgress;
        private String targetProgress;
        private int presentNumber;
        private LocalDate startDate;
        private LocalDate endDate;
        private int price;
        private String alertDays;
        private String message;
        private int wastePrice; // TODO 날린돈 계산

        public static LessonDetailDto.Response create(Lesson lesson) {
            return Response.builder()
                    .lessonDetailId(lesson.getLessonId())
                    .isFinished(lesson.getIsFinished())
                    .restDate(lesson.getRestDate())
                    .name(lesson.getLessonName())
                    .category(lesson.getCategory().getCategoryName())
                    .site(lesson.getSite().getSiteName())
                    .presentProgress("40%") // TODO 계산하기
                    .targetProgress("50%")
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
        private Long lessonDetailId;
    }


}
