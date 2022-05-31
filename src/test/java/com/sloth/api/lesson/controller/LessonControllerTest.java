package com.sloth.api.lesson.controller;

import com.sloth.api.lesson.dto.LessonNumberDto;
import com.sloth.api.lesson.dto.LessonUpdateDto;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.creator.CategoryCreator;
import com.sloth.creator.LessonCreator;
import com.sloth.creator.MemberCreator;
import com.sloth.creator.SiteCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.lesson.Lesson;
import com.sloth.domain.member.Member;
import com.sloth.domain.member.service.MemberService;
import com.sloth.domain.site.Site;
import com.sloth.global.resolver.CurrentEmailArgumentResolver;
import com.sloth.global.util.DateTimeUtils;
import com.sloth.test.base.NewBaseApiController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LessonControllerTest  extends NewBaseApiController {

    @InjectMocks
    LessonController lessonController;

    @Mock
    MemberService memberService;

    @Mock
    LessonService lessonService;

    Member member;
    Site site;
    Category category;
    Lesson lesson;
    Long lessonId = 1L;
    
    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(lessonController)
                .setCustomArgumentResolvers(new CurrentEmailArgumentResolver())
                .build();

        LocalDate startDate = LocalDate.of(2021,10,1);
        LocalDate endDate = LocalDate.of(2021, 10, 31);
        site = SiteCreator.create(2L, "인프런");
        category = CategoryCreator.createStubCategory(1, "test", "test");
        member = MemberCreator.createMember(3L, testEmail);
        lesson = LessonCreator.createLesson(lessonId, "스프링부트 강의", startDate, endDate,category, site, 2, 10, member);
    }

    @Test
    @WithMockUser(roles = "USER")
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 적게")
    void plusPresentNumber_underTotal() throws Exception {

        //given
        LessonNumberDto.Request request = new LessonNumberDto.Request(lessonId, lesson.getPresentNumber());
        when(lessonService.updatePresentNumber(member.getEmail(), request.getLessonId(), request.getCount())).thenReturn(lesson);

        //when
        ResultActions result = mockMvc.perform(patch("/api/lesson/number")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                ;

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.isFinished", is(lesson.getIsFinished())))
                .andExpect(jsonPath("$.presentNumber", is(request.getCount())))
                ;
    }

    @Test
    @DisplayName("들은 강의 수 추가 - 총 강의수보다 많게")
    void plusPresentNumber_overTotal() throws Exception {

        //given
        LessonNumberDto.Request request = new LessonNumberDto.Request(lesson.getLessonId(), 12);
        lesson.updatePresentNumber(request.getCount());
        when(lessonService.updatePresentNumber(member.getEmail(), request.getLessonId(), request.getCount())).thenReturn(lesson);

        //when
        ResultActions result = mockMvc.perform(patch("/api/lesson/number")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                ;

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.presentNumber", is(lesson.getTotalNumber())))
                ;
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 초과")
    void minusPresentNumber_overZero() throws Exception {

        // given
        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), -1);
        lesson.updatePresentNumber(minusRequest.getCount());
        when(lessonService.updatePresentNumber(member.getEmail(), minusRequest.getLessonId(), minusRequest.getCount())).thenReturn(lesson);

        // when
        ResultActions result = mockMvc.perform(patch("/api/lesson/number")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minusRequest)))
                ;

        // then
        result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.presentNumber", is(lesson.getPresentNumber())))
                ;
    }

    @Test
    @DisplayName("들은 강의 수 감소 - 결과: 0 미만")
    void minusPresentNumber_underZero() throws Exception {

        // given
        LessonNumberDto.Request minusRequest = new LessonNumberDto.Request(lesson.getLessonId(), -4);
        lesson.updatePresentNumber(minusRequest.getCount());
        when(lessonService.updatePresentNumber(member.getEmail(), minusRequest.getLessonId(), minusRequest.getCount())).thenReturn(lesson);


        // when
        ResultActions result = mockMvc.perform(patch("/api/lesson/number")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minusRequest)))
                ;

        // then
        result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.presentNumber", is(lesson.getPresentNumber())))
                ;
    }

    @Test
    @DisplayName("강의 상세 조회")
    void getLessonDetail() throws Exception {

        //given
        BDDMockito.when(lessonService.findLessonWithSiteCategory(member.getEmail(), lessonId)).thenReturn(lesson);

        //when
        ResultActions result = mockMvc.perform(get("/api/lesson/detail/"+ lessonId)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                ;

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.remainDay", is(lesson.getRemainDay(LocalDate.now()))))
                .andExpect(jsonPath("$.categoryName", is(category.getCategoryName())))
                .andExpect(jsonPath("$.siteName", is(site.getSiteName())))
                .andExpect(jsonPath("$.lessonName", is(lesson.getLessonName())))
                .andExpect(jsonPath("$.currentProgressRate", is(lesson.getCurrentProgressRate())))
                .andExpect(jsonPath("$.goalProgressRate", is(lesson.getGoalProgressRate(LocalDate.now()))))
                .andExpect(jsonPath("$.totalNumber", is(lesson.getTotalNumber())))
                .andExpect(jsonPath("$.isFinished", is(lesson.getIsFinished())))
                .andExpect(jsonPath("$.presentNumber", is(lesson.getPresentNumber())))
                .andExpect(jsonPath("$.message", is(lesson.getMessage())))
                .andExpect(jsonPath("$.wastePrice", is(lesson.getWastePrice(LocalDate.now()))))
        ;
    }

    @Test
    @DisplayName("강의 수정 API 테스트")
    void updateLesson() throws Exception {

        // given
        LessonUpdateDto.Request request = new LessonUpdateDto.Request();
        request.setLessonName(lesson.getLessonName());
        request.setCategoryId(category.getCategoryId());
        request.setSiteId(site.getSiteId());
        request.setTotalNumber(20);
        request.setPrice(lesson.getPrice());

        when(lessonService.updateLesson(member.getEmail(), request, lessonId)).thenReturn(lesson);

        // when
        ResultActions result = mockMvc.perform(patch("/api/lesson/" + lesson.getLessonId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.price", is(request.getPrice())))
                .andExpect(jsonPath("$.lessonName", is(request.getLessonName())))
                ;
    }

    @Test
    @DisplayName("강의 리스트 조회 API 테스트")
    void getLessonList() throws Exception {

        // given
        List<Lesson> lessons = new ArrayList<>();
        for(long i = 4; i< 8; i++) {
            lessons.add( createLesson(i, member, site, category));
        }
        lessons.get(0).updatePresentNumber(2);

        when(lessonService.getLessons(member.getEmail())).thenReturn(lessons);

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/lesson/list")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                ;

        // then
        Lesson lesson1 = lessons.get(0);
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[3]").exists())
                .andExpect(jsonPath("$[0].remainDay").value(equalTo(lesson1.getRemainDay(LocalDate.now()))))
                .andExpect(jsonPath("$[0].categoryName").value(equalTo(category.getCategoryName())))
                .andExpect(jsonPath("$[0].siteName").value(equalTo(site.getSiteName())))
                .andExpect(jsonPath("$[0].lessonName").value(equalTo(lesson1.getLessonName())))

                .andExpect(jsonPath("$[0].price").value(equalTo(lesson1.getPrice())))
                .andExpect(jsonPath("$[0].currentProgressRate").value(equalTo(lesson1.getCurrentProgressRate())))
                .andExpect(jsonPath("$[0].goalProgressRate").value(equalTo(lesson1.getGoalProgressRate(LocalDate.now()))))
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

        // given
        List<Lesson> lessons = new ArrayList<>();

        for(long i = 4; i < 6; i++) {
            Lesson lesson = createLesson(i, member, site, category);
            lesson.updateDate(LocalDate.now().plusDays(i - 5), LocalDate.now().plusMonths(6 - i));
            lessons.add(lesson);
        }
        Lesson lesson1 = lessons.get(0);

        BDDMockito.when(lessonService.getDoingLessons(member.getEmail())).thenReturn(lessons);

        // when
        ResultActions result = mockMvc.perform(get("/api/lesson/doing")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[0].presentNumber", is(lesson1.getPresentNumber())))
                .andExpect(jsonPath("$[0].lessonName", is(lesson1.getLessonName())))
                .andExpect(jsonPath("$[0].remainDay", is(lesson1.getRemainDay(LocalDate.now()))))
                .andExpect(jsonPath("$[0].siteName", is(site.getSiteName())))
                .andExpect(jsonPath("$[0].categoryName", is(category.getCategoryName())))
                .andExpect(jsonPath("$[0].presentNumber", is(lesson1.getPresentNumber())))
                .andExpect(jsonPath("$[0].untilTodayNumber", is(lesson1.getGoalNumber(LocalDate.now()))))
                ;
    }

    @Test
    @DisplayName("강의 삭제 API 테스트")
    void deleteLesson() throws Exception {

        // given
        given(memberService.findByEmail(member.getEmail())).willReturn(member);

        //when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/lesson/" + lesson.getLessonId())
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                ;

        //then
        result.andExpect(status().isOk())
        ;

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

}