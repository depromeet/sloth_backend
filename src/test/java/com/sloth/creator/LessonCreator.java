package com.sloth.creator;

import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.Member;
import com.sloth.domain.site.Site;

import java.time.LocalDate;

public class LessonCreator {

    public static Lesson createLesson(String lessonName, LocalDate startDate, LocalDate endDate, Category category, Site site, int presentNumber, int totalNumber) {
        return Lesson.builder()
                .lessonName(lessonName)
                .presentNumber(presentNumber)
                .totalNumber(totalNumber)
                .category(category)
                .price(50000)
                .startDate(startDate)
                .endDate(endDate)
                .site(site)
                .build();
    }

    public static Lesson createLesson(Long lessonId, String lessonName,
                                      LocalDate startDate, LocalDate endDate, Category category,
                                      Site site, int presentNumber, int totalNumber,
                                      Member member) {
        return Lesson.builder()
                .lessonId(lessonId)
                .lessonName(lessonName)
                .presentNumber(presentNumber)
                .totalNumber(totalNumber)
                .category(category)
                .price(50000)
                .startDate(startDate)
                .endDate(endDate)
                .site(site)
                .member(member)
                .build();
    }

}
