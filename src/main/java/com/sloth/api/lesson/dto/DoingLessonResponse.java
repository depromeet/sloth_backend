package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoingLessonResponse {
    private Long id;
    private Boolean weeklyFinished;
    private int restDate;
    private String site;
    private String category;
    private int presentNumber;
    private int todoNumber;

    public static DoingLessonResponse create (Lesson lesson){
        return DoingLessonResponse.builder()
                .id(lesson.getId())
                .weeklyFinished(false) // TODO 계산 필요
                .restDate(lesson.getRestDate())// });
                .site(lesson.getSite().getName())
                .category(lesson.getCategory().getName())
                .presentNumber(lesson.getPresentNumber())
                .todoNumber(lesson.getTotalNumber() - lesson.getPresentNumber())
                .build();
    }
}
