package com.sloth.api.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysema.commons.lang.Assert;
import com.nimbusds.oauth2.sdk.token.AccessToken;
import com.sloth.api.lesson.dto.LessonDetailRequest;
import com.sloth.api.lesson.dto.LessonDetailResponse;
import com.sloth.api.lesson.dto.LessonNumberRequest;
import com.sloth.api.oauth.dto.SocialType;
import com.sloth.config.auth.TokenProvider;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.lesson.repository.LessonRepository;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.constant.Role;
import com.sloth.domain.member.repository.MemberRepository;
import com.sloth.domain.site.Site;
import com.sloth.domain.site.repository.SiteRepository;
import com.sloth.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.objectweb.asm.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
@Transactional
class LessonControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired LessonRepository lessonRepository;
    @Autowired SiteRepository siteRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired TokenProvider tokenProvider;
    @Autowired EntityManager em;

    protected String accessToken;

    @BeforeEach
    void init() {
        String email = "test@gmail.com";
        Date accessTokenExpireTime = tokenProvider.createAccessTokenExpireTime();
        accessToken = tokenProvider.createAccessToken(email, accessTokenExpireTime);
    }

    @BeforeAll
    public static void beforeAll(@Autowired LessonRepository lessonRepository,
                                 @Autowired MemberRepository memberRepository,
                                 @Autowired SiteRepository siteRepository,
                                 @Autowired CategoryRepository categoryRepository) {
        Member testMember = createTestMember(memberRepository);
        Site testSite = createSite(siteRepository);
        Category testCategory = createCategory(categoryRepository);
        createLesson(lessonRepository, testMember, testSite, testCategory);
    }

    private static Category createCategory(CategoryRepository categoryRepository) {
        Category category = new Category("testCategory", 0, null, null);
        return categoryRepository.save(category);
    }

    private static Site createSite(SiteRepository siteRepository) {
        Site site = new Site("testSite");
        return siteRepository.save(site);
    }

    private static void createLesson(LessonRepository lessonRepository, Member testMember, Site testSite, Category testCategory) {
        for (int i = 0; i < 5; i++) {
            Lesson lesson = Lesson.builder()
                    .member(testMember)
                    .name("testLesson" + i)
                    .startDate(LocalDate.now())
                    .endDate(LocalDate.now().plusMonths(1))
                    .totalNumber(10)
                    .price(200000)
                    .alertDays("월")
                    .site(testSite)
                    .category(testCategory)
                    .build();
            lessonRepository.save(lesson);
        }
    }

    private static Member createTestMember(MemberRepository memberRepository) {
        return memberRepository.save(Member.builder()
                .name("testMember")
                .email("email@email.com")
                .socialType(SocialType.KAKAO)
                .lessons(new ArrayList<>())
                .password("123123aa")
                .role(Role.ADMIN)
                .build());
    }

    @AfterAll
    public static void afterAll(@Autowired LessonRepository lessonRepository,
                                @Autowired MemberRepository memberRepository) {
        lessonRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 적게")
    void plusPresentNumber_underTotal() throws Exception {
        //given
        Lesson lesson = lessonRepository.findByName("testLesson2").get();
        LessonNumberRequest request = new LessonNumberRequest(lesson.getId(), 2);

        //when
        mockMvc.perform(post("/api/lesson/number/plus")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                //then
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        assertEquals(2, lesson.getPresentNumber());
        assertFalse(lesson.getIsFinished());
    }

    @Test
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 많게")
    void plusPresentNumber_overTotal() throws Exception {
        //given
        Lesson lesson = lessonRepository.findByName("testLesson2").get();
        LessonNumberRequest request = new LessonNumberRequest(lesson.getId(), 12);

        //when
        mockMvc.perform(post("/api/lesson/number/plus")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

        //then
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        assertEquals(lesson.getTotalNumber(), lesson.getPresentNumber());
        assertTrue(lesson.getIsFinished());
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 초과")
    void minusPresentNumber_overZero() throws Exception {
        //given
        Lesson lesson = lessonRepository.findByName("testLesson2").get();
        LessonNumberRequest plusRequest = new LessonNumberRequest(lesson.getId(), 4);
        LessonNumberRequest minusRequest = new LessonNumberRequest(lesson.getId(), 1);

        mockMvc.perform(post("/api/lesson/number/plus")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(plusRequest)));

        //when
        mockMvc.perform(post("/api/lesson/number/minus")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minusRequest)))

        //then
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        assertEquals(3, lesson.getPresentNumber());
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 미만")
    void minusPresentNumber_underZero() throws Exception {
        //given
        Lesson lesson = lessonRepository.findByName("testLesson2").get();
        LessonNumberRequest minusRequest = new LessonNumberRequest(lesson.getId(), 4);

        //when
        mockMvc.perform(post("/api/lesson/number/minus")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minusRequest)))

                //then
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        assertEquals(0, lesson.getPresentNumber());
    }

    @Test
    @DisplayName("강의 상세 조회")
    void getLessonDetail() throws Exception {
        //given
        Lesson lesson = lessonRepository.findByName("testLesson2").get();
        LessonDetailRequest request = new LessonDetailRequest(lesson.getId());

        //when
        MvcResult mvcResult = mockMvc.perform(get("/api/lesson/detail")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))

                //then
                .andExpect(status().isOk())
                .andReturn();
        HashMap resultMap = TestUtil.convert(mvcResult);
        assertEquals("testSite", resultMap.get("site"));
        assertEquals("testCategory", resultMap.get("category"));


    }
}