package com.sloth.api.lesson.dto;


import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.constant.LessonStatus;
import com.sloth.util.DateTimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;


@ApiModel(value = "강의 목록 조회 API 반환 객체", description = "강의 목록 조회 API 반환 객체")
public class LessonListDto {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    @Builder
    public static class Response {
        @ApiModelProperty(value = "강의 아이디")
        private Long lessonId;

        @ApiModelProperty(value = "강의 목표 남은일 수")
        private int remainDay;              // 인강 목표 남은 일수

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
        private String startDate;

        @ApiModelProperty(value = "강의 목표 종료일")
        private String endDate;

        @ApiModelProperty(value = "강의 개수")
        private int totalNumber;

        @ApiModelProperty(value = "강의 완강 성공 여부")
        private Boolean isFinished;

        @ApiModelProperty(value = "강의 상태 (CURRENT OR FINISH)")
        private LessonStatus lessonStatus;

        public static LessonListDto.Response create(Lesson lesson) {
            return Response.builder()
                    .lessonId(lesson.getLessonId())
                    .remainDay(lesson.getRemainDay())
                    .categoryName(lesson.getCategory().getCategoryName())
                    .siteName(lesson.getSite().getSiteName())
                    .lessonName(lesson.getLessonName())
                    .price(lesson.getPrice())
                    .currentProgressRate(lesson.getCurrentProgressRate())
                    .goalProgressRate(lesson.getGoalProgressRate())
                    .startDate(DateTimeUtils.convertToString(lesson.getStartDate()))
                    .endDate(DateTimeUtils.convertToString(lesson.getEndDate()))
                    .totalNumber(lesson.getTotalNumber())
                    .isFinished(lesson.getIsFinished())
                    .lessonStatus(lesson.getLessonStatus())
                    .build();
        }
    }

}
