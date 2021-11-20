package com.sloth.api.category.controller;

import com.sloth.api.BaseApiController;
import com.sloth.api.category.dto.CategoryDto;
import com.sloth.api.category.service.ApiCategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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

@WebMvcTest(controllers = ApiCategoryController.class)
public class ApiCategoryControllerTest extends BaseApiController {

    @MockBean
    private ApiCategoryService apiCategoryService;

    @Test
    @DisplayName("카테고리 리스트 조회 API 테스트")
    public void getCategorys() throws Exception {

        //given
        List<CategoryDto.Response> categoryResponses = new ArrayList<>();

        CategoryDto.Response categoryResponse1 = CategoryDto.Response.builder()
                .categoryId(1L)
                .categoryName("개발")
                .build();

        CategoryDto.Response categoryResponse2 = CategoryDto.Response.builder()
                .categoryId(2L)
                .categoryName("디자인")
                .build();

        categoryResponses.add(categoryResponse1);
        categoryResponses.add(categoryResponse2);

        given(apiCategoryService.getCategoryDtos())
                .willReturn(categoryResponses);

        //when
        ResultActions result = mockMvc.perform(get("/api/category/list")
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .accept(MediaType.APPLICATION_JSON));

        //then
        result.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists())
                .andExpect(jsonPath("$[0].categoryName").value(equalTo(categoryResponse1.getCategoryName())))
                .andExpect(jsonPath("$[0].categoryId").value(equalTo(1)))
                .andExpect(jsonPath("$[1].categoryName").value(equalTo(categoryResponse2.getCategoryName())))
                .andExpect(jsonPath("$[1].categoryId").value(equalTo(2)))
        ;
    }

}