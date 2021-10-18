package com.sloth.api.lesson.dto;

import com.sloth.domain.lesson.Lesson;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Data
@Builder
public class LessonDetailResponse {

    private Long id;
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

    public static LessonDetailResponse create(Lesson lesson) {
        return LessonDetailResponse.builder()
                .id(lesson.getId())
                .isFinished(lesson.getIsFinished())
                .restDate(lesson.getRestDate())
                .name(lesson.getName())
                .category(lesson.getCategory().getName())
                .site(lesson.getSite().getName())
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
