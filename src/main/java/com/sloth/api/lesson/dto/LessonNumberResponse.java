package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonNumberResponse {

    private Long id;
    private Integer presentNumber;
    private Boolean isFinished;
    private Boolean weeklyFinished;

    public static LessonNumberResponse create(Lesson lesson) {
        return LessonNumberResponse.builder()
                .id(lesson.getId())
                .presentNumber(lesson.getPresentNumber())
                .isFinished(lesson.getIsFinished())
                .build();
    }
}
