package com.sloth.domain.lesson;

import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.site.Site;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDate;

@Execution(ExecutionMode.CONCURRENT)
class LessonTest {

    @Test
    @DisplayName("남은 일수 계산")
    void getRemainDay() {
        //given
        LocalDate now = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", now, endDate,category, site,5, 30);

        //when
        int remainDay = lesson.getRemainDay(now);

        //then
        Assertions.assertThat(remainDay).isEqualTo(66);
    }

    @Test
    @DisplayName("목표 진도율 테스트")
    void getGoalProgressRateTest() {
        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,10,15);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,15, 30);

        // when
        int goalProgressRate = lesson.getGoalProgressRate(now);

        // then
        Assertions.assertThat(goalProgressRate).isEqualTo(50);
        System.out.println(lesson.getGoalProgressRate(now));
        System.out.println(lesson.getCurrentProgressRate());
        System.out.println(lesson.getWastePrice(now));
    }

    @Test
    @DisplayName("낭비된 가격 테스트")
    void getWastePriceTest() {
        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,10,15);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site, 5, 30);

        // when
        int goalProgressRate = lesson.getGoalProgressRate(now);

        // then
        Assertions.assertThat(lesson.getWastePrice(now)).isEqualTo(17000);
    }

}