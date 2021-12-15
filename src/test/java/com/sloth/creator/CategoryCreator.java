package com.sloth.creator;

import com.sloth.domain.category.Category;
import com.sloth.domain.nickname.Nickname;

public class CategoryCreator {

    public static Category createStubCategory(int categoryLvl, String categoryName, String rootCategoryName) {
        return Category.builder()
                .categoryId(1L)
                .rootCategoryId(1L)
                .categoryLvl(categoryLvl)
                .categoryName(categoryName)
                .rootCategoryName(rootCategoryName)
                .build();
    }

}
