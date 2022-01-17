package com.sloth.domain.lesson;

import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.constant.LessonStatus;
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
    @DisplayName("현재 날짜가 강의 시작일 이전일 경우 목표 진도율 테스트")
    void getGoalProgressRateTest2() {
        // given
        LocalDate startDate = LocalDate.of(2021,12,12);
        LocalDate endDate = LocalDate.of(2021, 12, 18);
        LocalDate now = LocalDate.of(2021,12,4);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,0, 24);

        // when
        int goalProgressRate = lesson.getGoalProgressRate(now);

        // then
        Assertions.assertThat(goalProgressRate).isEqualTo(0);
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

        // then
        Assertions.assertThat(lesson.getWastePrice(now)).isEqualTo(17000);
    }

    //@Test // todo 로직 수정 후 테스트 다시 추가
    @DisplayName("낭비된 가격 테스트 - 강의 수강 완료 후")
    void getWastePriceTest2() {
        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,11,15);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site, 30, 30);

        // then
        Assertions.assertThat(lesson.getWastePrice(now)).isEqualTo(0);
    }


    @Test
    @DisplayName("Lesson 상태 조회 테스트 (현재)")
    void getLessonStatus1() {

        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,10,31);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site, 5, 30);

        // when
        LessonStatus lessonStatus = lesson.getLessonStatus(now);

        // then
        Assertions.assertThat(lessonStatus).isEqualTo(LessonStatus.CURRENT);
    }

    @Test
    @DisplayName("Lesson 상태 조회 테스트 (현재)")
    void getLessonStatus2() {

        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,10,15);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site, 5, 30);

        // when
        LessonStatus lessonStatus = lesson.getLessonStatus(now);

        // then
        Assertions.assertThat(lessonStatus).isEqualTo(LessonStatus.CURRENT);
    }

    @Test
    @DisplayName("Lesson 상태 조회 테스트 (과거)")
    void getLessonStatus3() {

        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,11,20);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site, 5, 30);

        // when
        LessonStatus lessonStatus = lesson.getLessonStatus(now);

        // then
        Assertions.assertThat(lessonStatus).isEqualTo(LessonStatus.PAST);
    }

}