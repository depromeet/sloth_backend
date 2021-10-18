package com.sloth.api.lesson.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RequestLessonDto {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int price;
    private String alertDays;
    private Boolean isFinished;
    private int totalNumber;
    private Long memberId;
    private Long siteId;
    private Long categoryId;

}
