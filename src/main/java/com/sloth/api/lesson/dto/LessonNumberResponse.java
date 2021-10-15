package com.sloth.api.lesson.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonNumberResponse {

    private Long id;
    private Integer presentNumber;
    private Boolean isFinished;
    private Boolean weeklyFinished;
}
