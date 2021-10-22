package com.sloth.api.lesson.controller;

import com.sloth.api.BaseApiController;

import com.sloth.api.lesson.dto.LessonDetailDto;
import com.sloth.api.lesson.dto.LessonNumberDto;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LessonControllerTest extends BaseApiController {

    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    SiteRepository siteRepository;

    @MockBean
    CategoryRepository categoryRepository;

    private Category createCategory() {
        return new Category("개발", 1, 1l, "개발");
    }

    private Site createSite() {
        return new Site("testSite");
    }

    private Lesson createLesson(Member member, Site site, Category category) {
        return Lesson.builder()
                .member(member)
                .lessonName("testLesson")
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusMonths(1))
                .totalNumber(10)
                .price(200000)
                .alertDays("월")
                .site(site)
                .category(category)
                .build();
    }

    private List<Lesson> createLessons(Member member, Site site, Category category) {
        List<Lesson> lessons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Lesson lesson = Lesson.builder()
                    .member(member)
                    .lessonName("testLesson" + i)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(1))
                    .totalNumber(10)
                    .price(200000)
                    .alertDays("월")
                    .site(site)
                    .category(category)
                    .build();
            lessons.add(lesson);
        }

        return lessons;
    }

    private Member createTestMember() {
        return Member.builder()
                .memberName("testMember")
                .email("email@email.com")
                .socialType(SocialType.KAKAO)
                .lessons(new ArrayList<>())
                .password("123123aa")
                .role(Role.ADMIN)
                .build();
    }

    @Test
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 적게")
    void plusPresentNumber_underTotal() throws Exception {

        //given
        Member member = createTestMember();
        Site site = createSite();
        Category category = createCategory();
        Lesson lesson = createLesson(member, site, category);
        Optional<Lesson> optionalLesson = Optional.of(lesson);

        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request request = new LessonNumberDto.Request(1L, 2);

        //when
        mockMvc.perform(patch("/api/lesson/number/plus")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        assertEquals(2, lesson.getPresentNumber());
        assertFalse(lesson.getIsFinished());
    }

    @Test
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 많게")
    void plusPresentNumber_overTotal() throws Exception {

        //given
        Member member = createTestMember();
        Site site = createSite();
        Category category = createCategory();
        Lesson lesson = createLesson(member, site, category);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request request = new LessonNumberDto.Request(1L, 12);

        //when
        mockMvc.perform(patch("/api/lesson/number/plus")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //then
        assertEquals(lesson.getTotalNumber(), lesson.getPresentNumber());
        assertTrue(lesson.getIsFinished());
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 초과")
    void minusPresentNumber_overZero() throws Exception {

        //given
        Member member = createTestMember();
        Site site = createSite();
        Category category = createCategory();
        Lesson lesson = createLesson(member, site, category);
        lesson.plusPresentNumber(4);

        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(1L, 1);

        mockMvc.perform(patch("/api/lesson/number/minus")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(minusRequest)))
                .andExpect(status().isOk());

        //then
        assertEquals(3, lesson.getPresentNumber());
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 미만")
    void minusPresentNumber_underZero() throws Exception {

        //given
        Member member = createTestMember();
        Site site = createSite();
        Category category = createCategory();
        Lesson lesson = createLesson(member, site, category);
        lesson.plusPresentNumber(2);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(1L, 4);

        //when
        mockMvc.perform(patch("/api/lesson/number/minus")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(minusRequest)))

                //then
                .andExpect(status().isOk());

        assertEquals(0, lesson.getPresentNumber());
    }

    //@Test
    @DisplayName("강의 상세 조회")
    void getLessonDetail() throws Exception {

        //given
        Member member = createTestMember();
        Site site = createSite();
        Category category = createCategory();
        Lesson lesson = createLesson(member, site, category);
        lesson.plusPresentNumber(2);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonDetailDto.Request request = new LessonDetailDto.Request(1L);

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/lesson/detail")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        HashMap resultMap = TestUtil.convert(mvcResult);
        assertEquals("testSite", resultMap.get("site"));
        assertEquals("testCategory", resultMap.get("category"));
    }

}