package com.sloth.creator;

import com.sloth.domain.lesson.Lesson;

import java.time.LocalDate;

public class LessonCreator {

    public static Lesson createLesson(String lessonName, LocalDate startDate, LocalDate endDate) {
        return Lesson.builder()
                .lessonName(lessonName)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
