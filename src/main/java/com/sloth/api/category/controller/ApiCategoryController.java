package com.sloth.api.category.controller;

import com.sloth.api.category.dto.ResponseCategoryDto;
import com.sloth.api.category.service.ApiCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiCategoryController {

    private final ApiCategoryService apiCategoryService;

    @Operation(summary = "CATEGORY API", description = "카테고리 리스트 조회 API")
    @GetMapping("/categorys")
    public ResponseEntity<List<ResponseCategoryDto>> getCategorys() {
        List<ResponseCategoryDto> categoryDtos = apiCategoryService.getCategoryDtos();
        return ResponseEntity.ok().body(categoryDtos);
    }

}
