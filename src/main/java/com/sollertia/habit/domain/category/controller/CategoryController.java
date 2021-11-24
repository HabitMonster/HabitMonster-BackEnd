package com.sollertia.habit.domain.category.controller;

import com.sollertia.habit.domain.category.dto.CategoryResponseDto;
import com.sollertia.habit.domain.category.dto.CategoryVo;
import com.sollertia.habit.domain.category.enums.Category;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

//    @Cacheable(value = "Categories", key = "1")
    @ApiOperation(value = "Category 목록 조회")
    @GetMapping("/categories")
    public CategoryResponseDto categoryPresetList() {
        List<CategoryVo> list = Category.getCategories();
        return CategoryResponseDto.builder().categories(list).statusCode(200).responseMessage("Category Query Completed").build();
    }
}
