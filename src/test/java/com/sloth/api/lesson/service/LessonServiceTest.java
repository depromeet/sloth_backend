package com.sloth.api.lesson.service;

import com.sloth.api.lesson.dto.LessonCreateDto;
import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.api.login.form.dto.FormLoginRequestDto;
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
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.nickname.service.NicknameService;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.global.exception.BusinessException;
import com.sloth.global.exception.ForbiddenException;
import com.sloth.global.exception.NeedEmailConfirmException;
import com.sloth.global.exception.handler.GlobalExceptionHandler;
import com.sloth.global.util.DateTimeUtils;
import com.sloth.test.base.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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

    @Mock
    MemberService memberService;

    Lesson lesson;
    Member member;
    Site site;
    Category category;

    @BeforeEach
    void init() {
        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);

        member = MemberCreator.createMember(2L, "test@test.com");
        site = SiteCreator.create(3L, "?????????");
        category = CategoryCreator.createStubCategory(1, "test", "test");
        lesson = Mockito.spy(LessonCreator.createLesson(1L, "??????????????? ??????", startDate, endDate, category
                , site, 15, 30, member));
    }

    @Test
    @DisplayName("?????? ?????? ????????? - ?????? ?????????")
    void findLessonWithSiteCategoryTest() {

        // given
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        when(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId())).thenReturn(optionalLesson);
        when(memberService.findByEmail(member.getEmail())).thenReturn(member);

        // when
        Lesson result = lessonService.findLessonWithSiteCategory(member.getEmail(), lesson.getLessonId());

        // then
        verify(optionalLesson.get(), times(1)).verifyOwner(member);
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLessonId()).isEqualTo(lesson.getLessonId());
    }

    @Test
    @DisplayName("?????? ?????? ????????? - ?????? ???????????? ?????? ??????")
    void findLessonWithSiteCategoryTest2() {

        // given
        when(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId()))
                .thenReturn(Optional.<Lesson>empty());

        // when && then
        when(memberService.findByEmail(member.getEmail())).thenReturn(member);
        Assertions.assertThatThrownBy(() -> {
            lessonService.findLessonWithSiteCategory(member.getEmail(), lesson.getLessonId());
        }).isInstanceOf(LessonNotFoundException.class).hasMessageContaining("???????????? ????????? ?????? ??? ????????????.");

    }

    @Test
    @DisplayName("lesson ?????? remainDay ???????????? ?????? ?????????")
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
    @DisplayName("?????? ???????????? ????????? - ?????? ?????????")
    void updatePresentNumberTest() {

        // given
        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("??????????????? ??????");
        request.setTotalNumber(1);
        request.setSiteId(site.getSiteId());
        request.setCategoryId(category.getCategoryId());

        Optional<Lesson> optionalLesson = Optional.of(this.lesson);
        when(lessonRepository.findWithMemberByLessonId(this.lesson.getLessonId()))
                .thenReturn(optionalLesson);

        Optional<Site> optionalSite = Optional.of(this.site);
        when(siteRepository.findById(request.getSiteId()))
                .thenReturn(optionalSite);

        Optional<Category> optionalCategory = Optional.of(this.category);
        when( categoryRepository.findById(request.getCategoryId()))
                .thenReturn(optionalCategory);

        when(memberService.findByEmail(member.getEmail())).thenReturn(member);

        // when
        Lesson result = lessonService.updateLesson(member.getEmail(), request, this.lesson.getLessonId());

        // then
        Assertions.assertThat(result.getTotalNumber()).isEqualTo(request.getTotalNumber());
    }

   @Test
   @DisplayName("?????? ??????_??????_?????????")
   void save_lesson_unavailable_site() {
        //given
        Long requestSiteId = 1L;
       given(siteRepository.findById(requestSiteId)).willReturn(Optional.empty());

        //when
       final BusinessException businessException = assertThrows(BusinessException.class, () -> {
           lessonService.saveLesson(lesson, requestSiteId, 2L);
       });

       assertEquals("???????????? ???????????? ????????????.", businessException.getMessage());
   }

    @Test
    @DisplayName("?????? ??????_??????_????????????")
    void save_lesson_unavailable_category() {
        //given
        Long requestSiteId = 1L;
        Long requestCategoryId = 2L;
        given(siteRepository.findById(requestSiteId)).willReturn(Optional.of(site));
        given(categoryRepository.findById(requestCategoryId)).willReturn(Optional.empty());

        //when
        final BusinessException businessException = assertThrows(BusinessException.class, () -> {
            lessonService.saveLesson(lesson, requestSiteId, requestCategoryId);
        });

        assertEquals("??????????????? ???????????? ????????????.", businessException.getMessage());
    }

    @Test
    @DisplayName("?????? ??????")
    void save_lesson() {
        //given
        Long requestSiteId = 1L;
        Long requestCategoryId = 2L;
        given(siteRepository.findById(requestSiteId)).willReturn(Optional.of(site));
        given(categoryRepository.findById(requestCategoryId)).willReturn(Optional.of(category));
        given(lessonRepository.save(lesson)).willReturn(lesson);
        doNothing().when(lesson).updateSite(site);
        doNothing().when(lesson).updateCategory(category);

        //when
        final Long savedLessonId = lessonService.saveLesson(lesson, requestSiteId, requestCategoryId);

        //then
        verify(lesson, times(1)).updateSite(site);
        verify(lesson).updateCategory(category);
        assertEquals(lesson.getLessonId(), savedLessonId);
    }

    @Test
    @DisplayName("????????? ?????? ??? ???????????? - ?????? ?????? id")
    void update_present_number_not_found_lesson() {
        //given
        Long requestLessonId = 1L;
        final String message = "???????????? ????????? ?????? ??? ????????????.";
        given(lessonRepository.findWithMemberByLessonId(requestLessonId)).willReturn(Optional.empty());

        //when
        final LessonNotFoundException lessonNotFoundException = assertThrows(LessonNotFoundException.class, () -> {
            lessonService.updatePresentNumber(member.getEmail(), requestLessonId, 10);
        });

        assertEquals(message, lessonNotFoundException.getMessage());
    }

    @Test
    @DisplayName("????????? ?????? ??? ????????????")
    void update_present_number() {
        //given
        Long requestLessonId = 1L;
        final int count = 10;
        given(lessonRepository.findWithMemberByLessonId(requestLessonId)).willReturn(Optional.of(lesson));
        given(memberService.findByEmail(member.getEmail())).willReturn(member);

        doNothing().when(lesson).verifyOwner(member);
        doNothing().when(lesson).updatePresentNumber(count);

        //when
        final Lesson updatedLesson = lessonService.updatePresentNumber(member.getEmail(), requestLessonId, count);

        //then
        verify(lesson, times(1)).verifyOwner(member);
        verify(lesson, times(1)).updatePresentNumber(count);
        assertEquals(lesson.getLessonId(), updatedLesson.getLessonId());
    }

    @Test
    @DisplayName("?????? ???????????? ?????? ????????? ??????")
    void get_doing_lessons() {
        //given
        List<Lesson> lessons = new ArrayList<>();
        given(lessonRepository.getDoingLessonsDetail(member.getMemberId())).willReturn(lessons);
        given(memberService.findByEmail(member.getEmail())).willReturn(member);

        //when
        final List<Lesson> doingLessons = lessonService.getDoingLessons(member.getEmail());

        //then
        assertEquals(lessons, doingLessons);
        verify(lessonRepository).getDoingLessonsDetail(member.getMemberId());
    }

    @Test
    @DisplayName("?????? ?????? ????????? ??????")
    void get_lessons() {
        //given
        List<Lesson> lessons = new ArrayList<>();
        given(lessonRepository.getLessons(member.getMemberId())).willReturn(lessons);
        given(memberService.findByEmail(member.getEmail())).willReturn(member);

        //when
        final List<Lesson> doingLessons = lessonService.getLessons(member.getEmail());

        //then
        assertEquals(lessons, doingLessons);
        verify(lessonRepository).getLessons(member.getMemberId());
    }

    @Test
    @DisplayName("?????? ??????")
    void delete_lesson() {
        //given
        Long requestLessonId = 1L;
        given(lessonRepository.findById(requestLessonId)).willReturn(Optional.of(lesson));
        doNothing().when(lessonRepository).delete(lesson);

        //when
        lessonService.deleteLesson(member, requestLessonId);

        //then
        verify(lesson).verifyOwner(member);
        verify(lessonRepository).delete(lesson);
    }

}