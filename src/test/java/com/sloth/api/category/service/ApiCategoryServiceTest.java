package com.sloth.api.category.service;

import com.sloth.api.category.dto.CategoryDto;
import com.sloth.creator.CategoryCreator;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import com.sloth.test.base.BaseServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.when;

public class ApiCategoryServiceTest extends BaseServiceTest {

    @InjectMocks
    private ApiCategoryService apiCategoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("카테고리 리스트 조회 테스트")
    public void getCategoryDtosTest() {

        // given
        List<Category> categorys = new ArrayList<>();
        for(long i=1;i<=3;i++) {
            String categoryName = "개발" + i;
            categorys.add(CategoryCreator.createCategory(i, i,0, categoryName, categoryName));
        }
        when(categoryRepository.findAll()).thenReturn(categorys);

        // when
        List<CategoryDto.Response> resultList = apiCategoryService.getCategoryDtos();

        // then
        Assertions.assertThat(resultList.size()).isEqualTo(3);
        Assertions.assertThat(resultList.get(0).getCategoryId()).isEqualTo(categorys.get(0).getCategoryId());
        Assertions.assertThat(resultList.get(0).getCategoryName()).isEqualTo(categorys.get(0).getCategoryName());
    }

}