package com.sloth.domain.lesson;

import com.sloth.creator.LessonCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class LessonTest {

    @Test
    @DisplayName("남은 일수 계산")
    void getRemainDay() {
        //given
        LocalDate now = LocalDate.of(2021,11,20);
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", now, LocalDate.of(2022, 1, 25));

        //when
        int remainDay = lesson.getRemainDay(now);

        //then
        Assertions.assertThat(66).isEqualTo(remainDay);
    }

}