package com.sloth.api.lesson.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ApiModel(value = "강의 완료 확인 반환 객체", description = "강의 완료 확인 반환 객체")
public class FinishedLessonDto {

    @ApiModelProperty(value = "강의 완료 여부")
    private Boolean isFinished;

}

