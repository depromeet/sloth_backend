package com.sloth.api.lesson.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.sloth.global.util.DateTimeUtils;
import com.sloth.global.util.TestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class LessonControllerTest  {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1MiLCJhdWQiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MzcxNjM3NjUsImV4cCI6MTE2MzcxNjM3NjV9.9fwwJ6FC_36WwZi2AyAV1VY6SkVdyO6G7Mmr6B9MtSvy4SIwPyWl3G8qUjoZzy4g7gSpRqV-0kQBdB8t2Mm2Tw";

    protected final String testEmail = "email@email.com";


    @MockBean
    LessonRepository lessonRepository;

    @MockBean
    SiteRepository siteRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @MockBean
    MemberRepository memberRepository;

    private Member member;
    private Site site;
    private Category category;
    private Lesson lesson;
    private Optional<Lesson> optionalLesson;
    
    @BeforeEach
    void beforeEach() {
        member = createTestMember(1L);
        site = createSite(1L);
        category = createCategory(1L);
        lesson = createLesson(1L, member, site, category);
        optionalLesson = Optional.of(lesson);

        given(lessonRepository.findById(lesson.getLessonId()))
                .willReturn(optionalLesson);

        given(lessonRepository.findWithMemberByLessonId(lesson.getLessonId()))
                .willReturn(optionalLesson);

        given(memberRepository.findByEmail(testEmail))
                .willReturn(Optional.of(member));

        given(lessonRepository.findWithSiteCategoryMemberByLessonId(lesson.getLessonId()))
                .willReturn(optionalLesson);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 적게")
    void plusPresentNumber_underTotal() throws Exception {

        //given

        LessonNumberDto.Request request = new LessonNumberDto.Request(lesson.getLessonId(), 2);

        //when
        mockMvc.perform(patch("/api/lesson/number")
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

        LessonNumberDto.Request request = new LessonNumberDto.Request(lesson.getLessonId(), 12);

        //when
        mockMvc.perform(patch("/api/lesson/number")
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
        
        lesson.updatePresentNumber(4);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), -1);

        mockMvc.perform(patch("/api/lesson/number")
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

        lesson.updatePresentNumber(2);

        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), -4);

        //when
        mockMvc.perform(patch("/api/lesson/number")
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
        
        lesson.updatePresentNumber(2);

        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("업데이트 강의명");
        request.setCategoryId(2L);
        request.setSiteId(1L);
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
    @DisplayName("강의 수정 API 테스트")
    void updateLesson() throws Exception {

        //given
        Optional<Site> optionalSite = Optional.of(site);
        Optional<Category> optionalCategory = Optional.of(category);

        given(siteRepository.findById(site.getSiteId()))
                .willReturn(optionalSite);

        given(categoryRepository.findById(category.getCategoryId()))
                .willReturn(optionalCategory);

        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName("lesson name update");
        request.setCategoryId(category.getCategoryId());
        request.setSiteId(site.getSiteId());
        request.setTotalNumber(20);

        //when
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.patch("/api/lesson/" + lesson.getLessonId())
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

    @Test
    @DisplayName("강의 리스트 조회 API 테스트")
    void getLessonList() throws Exception {

        //given
        Optional<Member> optionalMember = Optional.of(member);

        List<Lesson> lessons = new ArrayList<>();

        for(long i = 4; i< 8; i++) {
            lessons.add( createLesson(i, member, site, category));
        }
        lessons.get(0).updatePresentNumber(2);

        given(lessonRepository.getLessons(member.getMemberId()))
                .willReturn(lessons);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/lesson/list")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                ;

        //then
        Lesson lesson1 = lessons.get(0);
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[3]").exists())
                .andExpect(jsonPath("$[0].remainDay").value(equalTo(lesson1.getRemainDay(LocalDate.now()))))
                .andExpect(jsonPath("$[0].categoryName").value(equalTo(category.getCategoryName())))
                .andExpect(jsonPath("$[0].siteName").value(equalTo(site.getSiteName())))
                .andExpect(jsonPath("$[0].lessonName").value(equalTo(lesson1.getLessonName())))

                .andExpect(jsonPath("$[0].price").value(equalTo(lesson1.getPrice())))
                //.andExpect(jsonPath("$[0].currentProgressRate").value(equalTo(lesson1.currentProgressRate())))
                //.andExpect(jsonPath("$[0].goalProgressRate").value(equalTo(lesson1.goalProgressRate())))
                .andExpect(jsonPath("$[0].startDate").value(equalTo(DateTimeUtils.convertToString(lesson1.getStartDate()))))
                .andExpect(jsonPath("$[0].endDate").value(equalTo(DateTimeUtils.convertToString(lesson1.getEndDate()))))
                .andExpect(jsonPath("$[0].totalNumber").value(equalTo(lesson1.getTotalNumber())))
                .andExpect(jsonPath("$[0].isFinished").value(equalTo(lesson1.getIsFinished())))
                .andExpect(jsonPath("$[0].lessonStatus").value(equalTo(lesson1.getLessonStatus(LocalDate.now()).name())))
                ;
    }

    @Test
    @DisplayName("투데이 강의 목록 조회 API 테스트")
    void getDoingLesson() throws Exception {
        //given
        Optional<Member> optionalMember = Optional.of(member);

        List<Lesson> lessons = new ArrayList<>();

        for(long i = 4; i < 6; i++) {
            Lesson lesson = createLesson(i, member, site, category);
            lesson.updateDate(LocalDate.now().plusDays(i - 5), LocalDate.now().plusMonths(6 - i));
            lessons.add(lesson);
            System.out.println("id: "+i + ", remain: "+lesson.getRemainDay(LocalDate.now()));
        }
        Lesson lesson1 = lessons.get(0);
        Lesson lesson2 = lessons.get(1);

        given(lessonRepository.getDoingLessonsDetail(member.getMemberId()))
                .willReturn(lessons);

        //when
        ResultActions result = mockMvc.perform(get("/api/lesson/doing")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                // remainDay 기준 정렬 검증
                .andExpect(jsonPath("$[0].remainDay").value(equalTo(lesson2.getRemainDay(LocalDate.now()))))
                .andExpect(jsonPath("$[1].remainDay").value(equalTo(lesson1.getRemainDay(LocalDate.now()))));
    }

    @Test
    @DisplayName("강의 삭제 API 테스트")
    void deleteLesson() throws Exception {

        //given
        //given(lessonRepository.delete(lesson));
        //        .willReturn(null);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/lesson/" + lesson.getLessonId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                ;

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
        ;

    }

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
                .email(testEmail)
                .socialType(SocialType.KAKAO)
                .lessons(new ArrayList<>())
                .password("123123aa")
                .role(Role.ADMIN)
                .build();
    }

}