package com.sloth.api.lesson.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
@ApiModel(value = "강의 통계 조회 객체", description = "강의 통계 조회 객체")
public class LessonStatsResponseDto {

    @ApiModelProperty(value = "만기된 강의 총 개수")
    private int expiredLessonsCnt;

    @ApiModelProperty(value = "만기된 강의 총 금액")
    private Long expiredLessonsPrice;

    @ApiModelProperty(value = "완료한 강의 총 개수")
    private int finishedLessonsCnt;

    @ApiModelProperty(value = "완료한 강의 총 금액")
    private Long finishedLessonsPrice;

    @ApiModelProperty(value = "완료하지 못한 강의 총 개수")
    private int notFinishedLessonsCnt;

    @ApiModelProperty(value = "완료하지 못한 강의 총 금액")
    private Long notFinishedLessonsPrice;

}
