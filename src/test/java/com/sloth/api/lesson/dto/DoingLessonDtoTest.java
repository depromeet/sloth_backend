package com.sloth.api.lesson.dto;

import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.site.Site;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class DoingLessonDtoTest {

    @Test
    @DisplayName("반환 객체 테스트")
    void responseCreateTest(){
        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LocalDate now = LocalDate.of(2021,10,15);

        Site site = SiteCreator.create("인프런");
        Category category = CategoryCreator.createStubCategory(1, "test", "test");
        Lesson lesson = LessonCreator.createLesson("스프링부트 강의", startDate, endDate, category, site, 15, 30);

        // when
        DoingLessonDto.Response doingLessonResponse = DoingLessonDto.Response.create(lesson, now);

        // then
        Assertions.assertThat(doingLessonResponse.getUntilTodayNumber()).isEqualTo(15);
    }

}