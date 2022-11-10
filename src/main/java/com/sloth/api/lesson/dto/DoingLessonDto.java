package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class DoingLessonDto {

    @Data
    @Builder
    @ApiModel(value = "진행 중인 강의 반환 객체", description = "진행 중인 강의 반환 객체")
    public static class Response {
        @ApiModelProperty(value = "강의 아이디")
        private Long lessonId;

        @ApiModelProperty(value = "강의명")
        private String lessonName;

        @ApiModelProperty(value = "오늘까지 목표 달성 여부 (ex. 30일동안 30개 들어야 한다면 15일째에 15개 이상 들었을 때 true)")
        private Boolean untilTodayFinished;

        @ApiModelProperty(value = "목표 완강 날까지 남은 일 수")
        private int remainDay;

        @ApiModelProperty(value = "사이트명")
        private String siteName;

        @ApiModelProperty(value = "카테고리명")
        private String categoryName;

        @ApiModelProperty(value = "완강한 강의 개수")
        private int presentNumber;

        @ApiModelProperty(value = "시작부터 현재까지 들어야 하는 강의 누적 개수 (ex. 30일동안 60개 들어야 한다면 15일째에 30)")
        private int untilTodayNumber;

        @ApiModelProperty(value = "총 강의 수")
        private int totalNumber;

        public static DoingLessonDto.Response create (Lesson lesson, LocalDate now) {

            int goalNumber = lesson.getGoalNumber(now);

            return Response.builder()
                    .lessonName(lesson.getLessonName())
                    .lessonId(lesson.getLessonId())
                    .untilTodayFinished(lesson.getPresentNumber() >= goalNumber)
                    .remainDay(lesson.getRemainDay(now))
                    .siteName(lesson.getSite().getSiteName())
                    .categoryName(lesson.getCategory().getCategoryName())
                    .presentNumber(lesson.getPresentNumber())
                    .untilTodayNumber(goalNumber)
                  .build();
        }

    }

}
