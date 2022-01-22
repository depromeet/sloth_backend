package com.sloth.domain.category;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryTest {

    @Test
    @DisplayName("카테고리 생성자 테스트")
    public void Category() {

        // given
        Category category = new Category("개발", 0, 1L, "개발");

        // then
        Assertions.assertThat(category.getCategoryName()).isEqualTo("개발");
        Assertions.assertThat(category.getCategoryLvl()).isEqualTo(0);
        Assertions.assertThat(category.getRootCategoryId()).isEqualTo(1L);
        Assertions.assertThat(category.getRootCategoryName()).isEqualTo("개발");

    }

}