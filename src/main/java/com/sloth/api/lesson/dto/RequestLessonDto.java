package com.sloth.api.lesson.dto;

import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.site.Site;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Builder
    public RequestLessonDto(String name, LocalDate startDate, LocalDate endDate, int totalNumber, int price, String alertDays) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.totalNumber = totalNumber;
        this.alertDays = alertDays;

        this.isFinished = false;
    }

    public Lesson toEntity() {
        return Lesson.builder()
                .name(name)
                .startDate(startDate)
                .endDate(endDate)
                .price(price)
                .totalNumber(totalNumber)
                .alertDays(alertDays)
                .build();
    }

}
