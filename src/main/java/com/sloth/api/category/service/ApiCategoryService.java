package com.sloth.api.category.service;

import com.sloth.api.category.dto.ResponseCategoryDto;
import com.sloth.domain.category.Category;
import com.sloth.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ApiCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ResponseCategoryDto> getCategoryDtos() {
        List<Category> categorys= categoryRepository.findAll();

        List<ResponseCategoryDto> categoryDtos = categorys.stream().map(category -> ResponseCategoryDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build()
        ).collect(Collectors.toList());

        return categoryDtos;
    }

}
