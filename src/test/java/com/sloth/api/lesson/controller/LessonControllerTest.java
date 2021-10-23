package com.sloth.api.lesson.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sloth.api.BaseApiController;
import com.sloth.api.lesson.dto.LessonNumberDto;
import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.constant.SocialType;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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

    @MockBean
    MemberRepository memberRepository;

    private Category createCategory(Long categoryId) {
        return Category.builder()
                .categoryId(categoryId)
                .categoryName("개발")
                .categoryLvl(0)
                .rootCategoryName("개발")
                .build();
    }

    private Site createSite(Long id) {
        return Site.builder()
                .siteId(id)
                .siteName("테스트 사이트")
                .build();
    }

    private Lesson createLesson(Long lessonId, Member member, Site site, Category category) {
        return Lesson.builder()
                .lessonId(lessonId)
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

    private Member createTestMember(Long memberId) {
        return Member.builder()
                .memberId(memberId)
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
        Member member = createTestMember(null);
        Site site = createSite(null);
        Category category = createCategory(null);
        Lesson lesson = createLesson(1L, member, site, category);
        Optional<Lesson> optionalLesson = Optional.of(lesson);

        given(lessonRepository.findById(lesson.getLessonId()))
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
        Member member = createTestMember(null);
        Site site = createSite(2L);
        Category category = createCategory(2L);
        Lesson lesson = createLesson(1L, member, site, category);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(lesson.getLessonId()))
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
        Member member = createTestMember(null);
        Site site = createSite(null);
        Category category = createCategory(null);
        Lesson lesson = createLesson(1L, member, site, category);
        lesson.plusPresentNumber(4);

        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), 1);

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
        Member member = createTestMember(null);
        Site site = createSite(null);
        Category category = createCategory(null);
        Lesson lesson = createLesson(1L, member, site, category);
        lesson.plusPresentNumber(2);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(1L))
                .willReturn(optionalLesson);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), 4);

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
        Member member = createTestMember(null);
        Site site = createSite(null);
        Category category = createCategory(null);
        Lesson lesson = createLesson(1L, member, site, category);
        lesson.plusPresentNumber(2);
        Optional<Lesson> optionalLesson = Optional.of(lesson);
        given(lessonRepository.findById(lesson.getLessonId()))
                .willReturn(optionalLesson);

        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("업데이트 강의명");
        request.setCategoryId(2L);
        request.setSiteId(1L);
        request.setMemberId(1L);
        request.setTotalNumber(null);

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

    @Test
    @DisplayName("강위 수정 API 테스트")
    void updateLesson() throws Exception {

        //given
        Member member = createTestMember(1L);
        Site site = createSite(2L);
        Category category = createCategory(3L);
        Lesson lesson = createLesson(4L, member, site, category);

        Optional<Lesson> optionalLesson = Optional.of(lesson);
        Optional<Site> optionalSite = Optional.of(site);
        Optional<Category> optionalCategory = Optional.of(category);
        Optional<Member> optionalMember = Optional.of(member);

        given(memberRepository.findById(member.getMemberId()))
                .willReturn(optionalMember);

        given(siteRepository.findById(site.getSiteId()))
                .willReturn(optionalSite);

        given(lessonRepository.findById(lesson.getLessonId()))
                .willReturn(optionalLesson);

        given(categoryRepository.findById(category.getCategoryId()))
                .willReturn(optionalCategory);

        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("업데이트 강의명");
        request.setCategoryId(category.getCategoryId());
        request.setSiteId(site.getSiteId());
        request.setMemberId(member.getMemberId());
        request.setTotalNumber(20);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/lesson/" + String.valueOf(lesson.getLessonId()))
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        HashMap resultMap = TestUtil.convert(mvcResult);
        assertEquals(request.getLessonName(), resultMap.get("lessonName"));
    }

}