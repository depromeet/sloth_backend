package com.sloth.api.category.controller;

import com.sloth.api.category.dto.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class CategoryController {

    @GetMapping("/categorys")
    public ResponseEntity<List<CategoryDto>> getCategorys() {
        List<CategoryDto> categoryDtos = new ArrayList<>();

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setCategoryId(1L);
        categoryDto1.setCategroyName("개발");

        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setCategoryId(2L);
        categoryDto2.setCategroyName("디자인");

        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto3.setCategoryId(3L);
        categoryDto3.setCategroyName("데이터");

        CategoryDto categoryDto4 = new CategoryDto();
        categoryDto4.setCategoryId(4L);
        categoryDto4.setCategroyName("외국어");

        categoryDtos.add(categoryDto1);
        categoryDtos.add(categoryDto2);
        categoryDtos.add(categoryDto3);
        categoryDtos.add(categoryDto4);

        return ResponseEntity.ok().body(categoryDtos);
    }

}
