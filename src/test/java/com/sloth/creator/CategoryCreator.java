package com.sloth.creator;

import com.sloth.domain.category.Category;

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

    public static Category createCategory(Long categoryId, Long rootCategoryId, int categoryLvl, String categoryName, String rootCategoryName) {
        return Category.builder()
                .categoryId(categoryId)
                .rootCategoryId(rootCategoryId)
                .categoryLvl(categoryLvl)
                .categoryName(categoryName)
                .rootCategoryName(rootCategoryName)
                .build();
    }

}
