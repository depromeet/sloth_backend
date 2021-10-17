package com.sloth.api.lesson.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "강의 목록 조회 API 반환 객체", description = "강의 목록 조회 API 반환 객체")
public class ResponseMainLessonDto {

    @ApiModelProperty(value = "강의 아이디")
    private Long lessonId;

    @ApiModelProperty(value = "강의 타입 (CURRENT OR FINISH)")
    private String type;                // 진행중인지 지난 강의인지 구분 타입

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
    private double currentProgressRate;

    @ApiModelProperty(value = "강의 목표 진도율")
    private double goalProgressRate;

    @ApiModelProperty(value = "강의 시작일")
    private String startDate;

    @ApiModelProperty(value = "강의 목표 종료일")
    private String endDate;

    @ApiModelProperty(value = "강의 개수")
    private int totalNumber;

    @ApiModelProperty(value = "강의 완강 성공 여부")
    private Boolean isSuccess;

}
