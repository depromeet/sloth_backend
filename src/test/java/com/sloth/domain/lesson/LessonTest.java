package com.sloth.domain.lesson;

import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.constant.LessonStatus;
import com.sloth.domain.site.Site;
import com.sloth.global.exception.InvalidParameterException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.time.LocalDate;

@Execution(ExecutionMode.CONCURRENT)
class LessonTest {

    @Test
    @DisplayName("현재 강의 수 업데이트 - 완강")
    void updatePresentNumberTest1() {

        // given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);
        int updatePresentNumber = 25;
        // when
        lesson.updatePresentNumber(updatePresentNumber);

        // then
        Assertions.assertThat(lesson.getIsFinished()).isEqualTo(true);
        Assertions.assertThat(lesson.getPresentNumber()).isEqualTo(30);
    }

    @Test
    @DisplayName("현재 강의 수 업데이트 - 수강중")
    void updatePresentNumberTest2() {

        // given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);
        int updatePresentNumber = 20;
        // when
        lesson.updatePresentNumber(updatePresentNumber);

        // then
        Assertions.assertThat(lesson.getIsFinished()).isEqualTo(false);
        Assertions.assertThat(lesson.getPresentNumber()).isEqualTo(25);
    }

    @Test
    @DisplayName("현재 강의 수 업데이트 - 완강 후 수정")
    void updatePresentNumberTest3() {

        // given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);
        int updatePresentNumber = 25;
        lesson.updatePresentNumber(updatePresentNumber);

        // when
        lesson.updatePresentNumber(3);

        // then
        Assertions.assertThat(lesson.getIsFinished()).isEqualTo(true);
        Assertions.assertThat(lesson.getPresentNumber()).isEqualTo(30);
    }

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
    void getWastePriceTest1() {
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

    @Test
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

    @Test
    @DisplayName("현재 진행중인 강의인지 테스트")
    void isDoingLessonTest1() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);
        LocalDate now = LocalDate.of(2021,12,1);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        boolean doingLesson = lesson.isDoingLesson(now);

        // then
        Assertions.assertThat(doingLesson).isEqualTo(true);
    }

    @Test
    @DisplayName("현재 끝난 강의인지 테스트")
    void isDoingLessonTest2() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);
        LocalDate now = LocalDate.of(2022,2,1);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        boolean doingLesson = lesson.isDoingLesson(now);

        // then
        Assertions.assertThat(doingLesson).isEqualTo(false);
    }

    @Test
    @DisplayName("현재 시작 전 강의인지 테스트")
    void isDoingLessonTest3() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);
        LocalDate now = LocalDate.of(2021,10,4);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        boolean doingLesson = lesson.isDoingLesson(now);

        // then
        Assertions.assertThat(doingLesson).isEqualTo(false);
    }

    @Test
    @DisplayName("현재 진행중인 강의인지 테스트 - 시작일")
    void isDoingLessonTest4() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);
        LocalDate now = LocalDate.of(2021,11,20);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        boolean doingLesson = lesson.isDoingLesson(now);

        // then
        Assertions.assertThat(doingLesson).isEqualTo(true);
    }

    @Test
    @DisplayName("현재 진행중인 강의인지 테스트 - 종료일")
    void isDoingLessonTest5() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);
        LocalDate now = LocalDate.of(2022,1,25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        boolean doingLesson = lesson.isDoingLesson(now);

        // then
        Assertions.assertThat(doingLesson).isEqualTo(true);
    }

    @Test
    @DisplayName("시작일,종료일 수정 - 정상 케이스")
    void updateDateTest1() {

        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        LocalDate upadateStartDate = LocalDate.of(2021,12,20);
        LocalDate updateEndDate = LocalDate.of(2022, 2, 25);
        lesson.updateDate(upadateStartDate, updateEndDate);

        // then
        Assertions.assertThat(lesson.getStartDate()).isEqualTo(upadateStartDate);
        Assertions.assertThat(lesson.getEndDate()).isEqualTo(updateEndDate);
    }

    @Test
    @DisplayName("시작일,종료일 수정 - 시작일이 종료일보다 큰 경우")
    void updateDateTest2() {

        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when && then
        LocalDate upadateStartDate = LocalDate.of(2022,3,20);
        LocalDate updateEndDate = LocalDate.of(2022, 2, 25);
        Assertions.assertThatThrownBy(() -> {lesson.updateDate(upadateStartDate, updateEndDate);})
                .isInstanceOf(InvalidParameterException.class).hasMessageContaining("종료 일자는 시작 일자 이후여야 합니다.");
    }

    @Test
    @DisplayName("사이트 업데이트 테스트")
    void updateSiteTest() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create(1L, "인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        Site upadateSite = SiteCreator.create(2L, "패스트 캠퍼스");
        lesson.updateSite(upadateSite);

        // then
        Assertions.assertThat(lesson.getSite().getSiteId()).isEqualTo(upadateSite.getSiteId());
        Assertions.assertThat(lesson.getSite().getSiteName()).isEqualTo(upadateSite.getSiteName());
    }

    @Test
    @DisplayName("카테고리 업데이트 테스트")
    void updateCategoryTest() {
        //given
        LocalDate startDate = LocalDate.of(2021,11,20);
        LocalDate endDate = LocalDate.of(2022, 1, 25);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate,category, site,5, 30);

        // when
        Category updateCategory = CategoryCreator.createStubCategory(2, "test2", "test2");
        lesson.updateCategory(updateCategory);

        // then
        Assertions.assertThat(lesson.getCategory().getCategoryId()).isEqualTo(updateCategory.getCategoryId());
        Assertions.assertThat(lesson.getCategory().getRootCategoryId()).isEqualTo(updateCategory.getRootCategoryId());
        Assertions.assertThat(lesson.getCategory().getCategoryName()).isEqualTo(updateCategory.getCategoryName());
        Assertions.assertThat(lesson.getCategory().getCategoryLvl()).isEqualTo(updateCategory.getCategoryLvl());
    }


}