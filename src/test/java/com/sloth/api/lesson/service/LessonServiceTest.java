package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.LessonCreateDto;
import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.MemberCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.exception.LessonNotFoundException;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.global.exception.ForbiddenException;
import com.sloth.test.base.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LessonServiceTest extends BaseServiceTest {

    @InjectMocks LessonService lessonService;

    @Mock
    LessonRepository lessonRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    NicknameService nicknameService;
    @Mock
    SiteRepository siteRepository;
    @Mock
    CategoryRepository categoryRepository;

    Lesson lesson;
    Member member;
    Site site;
    Category category;

    @BeforeEach
    void init() {
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);

        member = MemberCreator.createMember(2L, "test@test.com");
        site = SiteCreator.create(3L, "인프런");
        category = CategoryCreator.createStubCategory(1, "test", "test");
        lesson = LessonCreator.createLesson(1L, "스프링부트 강의", startDate, endDate, category
                , site, 15, 30, member);
    }

    @Test
    @DisplayName("강의 조회 테스트 - 정상 케이스")
    void findLessonWithSiteCategoryTest() {

        // given
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        BDDMockito.when(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId())).thenReturn(optionalLesson);

        // when
        Lesson result = lessonService.findLessonWithSiteCategory(member, lesson.getLessonId());

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLessonId()).isEqualTo(lesson.getLessonId());

    }

    @Test
    @DisplayName("강의 조회 테스트 - 강의 존재하지 않는 경우")
    void findLessonWithSiteCategoryTest2() {

        // given
        BDDMockito.when(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId()))
                .thenReturn(Optional.<Lesson>empty());

        // when && then
        Assertions.assertThatThrownBy(() -> {
            lessonService.findLessonWithSiteCategory(member, lesson.getLessonId());
        }).isInstanceOf(LessonNotFoundException.class).hasMessageContaining("해당하는 강의를 찾을 수 없습니다.");

    }

    @Test
    @DisplayName("강의 조회 테스트 - 해당 강의 소유주아닐 경우 테스트")
    void findLessonWithSiteCategoryTest3() {

        // given
        Member member2 = MemberCreator.createMember(3L, "test2@test.com");
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        BDDMockito.when(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId())).thenReturn(optionalLesson);


        // when && then
        Assertions.assertThatThrownBy(() -> {
            lessonService.findLessonWithSiteCategory(member2, lesson.getLessonId());
        }).isInstanceOf(ForbiddenException.class).hasMessageContaining("해당 강의에 대한 권한이 없습니다.");

    }

    @Test
    @DisplayName("lesson 객체 remainDay 기준으로 정렬 테스트")
    void sortByRemainDay() {
        //given
        List<Lesson> lessons = new ArrayList<>();
        LocalDate now = LocalDate.now();
        for (int i = 0; i < 4; i++) {
            Lesson lesson = LessonCreator.createLesson("test" + i, now, now.plusDays(10 - i), null, null, 10, 100);
            lessons.add(lesson);
        }
        //when
        lessonService.sortByRemainDay(lessons);
        //then
        assertThat(lessons.get(0).getRemainDay(now)).isLessThanOrEqualTo(lessons.get(1).getRemainDay(now));
        assertThat(lessons.get(0).getLessonName()).isEqualTo("test3");
    }

    @Test
    @DisplayName("강의 업데이트 테스트 - 정상 케이스")
    void updatePresentNumberTest() {

        // given
        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("스프링부트 강의");
        request.setTotalNumber(1);
        request.setSiteId(site.getSiteId());
        request.setCategoryId(category.getCategoryId());

        Optional<Lesson> optionalLesson = Optional.of(this.lesson);
        BDDMockito.when(lessonRepository.findWithMemberByLessonId(this.lesson.getLessonId()))
                .thenReturn(optionalLesson);

        Optional<Site> optionalSite = Optional.of(this.site);
        BDDMockito.when(siteRepository.findById(request.getSiteId()))
                .thenReturn(optionalSite);

        Optional<Category> optionalCategory = Optional.of(this.category);
        BDDMockito.when( categoryRepository.findById(request.getCategoryId()))
                .thenReturn(optionalCategory);

        // when
        Lesson result = lessonService.updateLesson(member, request, this.lesson.getLessonId());

        // then
        Assertions.assertThat(result.getTotalNumber()).isEqualTo(request.getTotalNumber());
    }

    @Test
    @DisplayName("강의 생성 테스트")
    void saveLessonTest() {

        // given
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        LessonCreateDto.Request requestDto = new LessonCreateDto.Request();
        requestDto.setLessonName("스프링부트 강의");
        requestDto.setCategoryId(site.getSiteId());
        requestDto.setSiteId(category.getCategoryId());
        requestDto.setTotalNumber(100);
        requestDto.setMessage("");
        requestDto.setPrice(10000);
        requestDto.setAlertDays("");
        requestDto.setStartDate(startDate);
        requestDto.setEndDate(endDate);

        Optional<Site> optionalSite = Optional.of(this.site);
        BDDMockito.when(siteRepository.findById(requestDto.getSiteId()))
                .thenReturn(optionalSite);

        Optional<Category> optionalCategory = Optional.of(this.category);
        BDDMockito.when(categoryRepository.findById(requestDto.getCategoryId()))
                .thenReturn(optionalCategory);

        Lesson requestLesson = requestDto.toEntity(member);
        BDDMockito.when(lessonRepository.save(requestLesson)).thenReturn(lesson);

        // when
        Long lessonId = lessonService.saveLesson(requestLesson, requestDto.getSiteId(), requestDto.getCategoryId());

        // then
        Assertions.assertThat(lessonId).isEqualTo(lesson.getLessonId());
    }

}