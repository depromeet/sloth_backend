package com.sloth.api.category.controller;

import com.sloth.api.BaseApiController;
import com.sloth.api.lesson.service.LessonService;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class ApiCategoryControllerTest extends BaseApiController {

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    LessonService lessonService;

    private List<Category> createCategorys() {
        List<Category> categoryDtos = new ArrayList<>();
        Category category1 = Category.builder()
                .id(1L)
                .categoryLvl(1)
                .rootCategoryId(1L)
                .name("개발")
                .rootCategoryName("개발")
                .build();

        Category category2 = Category.builder()
                .id(2L)
                .categoryLvl(1)
                .rootCategoryId(2L)
                .name("디자인")
                .rootCategoryName("디자인")
                .build();


        categoryDtos.add(category1);
        categoryDtos.add(category2);

        return categoryDtos;
    }


    @Test
    @DisplayName("카테고리 리스트 조회 API 테스트")
    public void getCategorys() throws Exception {

        //given
        List<Category> categoryDtos = createCategorys();
        Category category1 = categoryDtos.get(0);
        Category category2 = categoryDtos.get(1);

        given(categoryRepository.findAll())
                .willReturn(categoryDtos);

        //when
        ResultActions result = mockMvc.perform(get("/api/categorys")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[0].categoryName").value(equalTo(category1.getName())))
                .andExpect(jsonPath("$[0].categoryId").value(equalTo(1)))
                .andExpect(jsonPath("$[1].categoryName").value(equalTo(category2.getName())))
                .andExpect(jsonPath("$[1].categoryId").value(equalTo(2)))
        ;
    }

}