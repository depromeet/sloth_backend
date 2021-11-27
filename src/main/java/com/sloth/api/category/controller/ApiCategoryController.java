package com.sloth.api.category.controller;

import com.sloth.api.category.dto.CategoryDto;
import com.sloth.api.category.service.ApiCategoryService;
import com.sloth.api.dto.ApiResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class ApiCategoryController {

    private final ApiCategoryService apiCategoryService;

    @GetMapping("/list")
    @Cacheable("categoryDtos")
    @Operation(summary = "카테고리 조회 API", description = "카테고리 리스트 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", defaultValue ="jwt access token", dataType = "string", value = "jwt access token", required = true, paramType = "header")
    })
    public ResponseEntity<List<CategoryDto.Response>> getCategorys() {
        List<CategoryDto.Response> categoryDtos = apiCategoryService.getCategoryDtos();
        return ResponseEntity.ok().body(categoryDtos);
    }

    @CacheEvict("categoryDtos")
    @DeleteMapping(value = "/cache", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "category list cache clear", description = "카테고리 리스트 캐시 삭제 api")
    public ResponseEntity<ApiResult> clearSiteCache( ) {
        return ResponseEntity.ok(ApiResult.createOk("category list cache clear success"));
    }

}
