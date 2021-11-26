package com.sollertia.habit.domain.category.enums;

import com.sollertia.habit.domain.category.dto.CategoryDto;
import com.sollertia.habit.global.exception.habit.InvalidCategoryException;

import java.util.ArrayList;
import java.util.List;

public enum Category {

    Health(1L), Study(2L), Life(3L),
    Emotion(4L), Relation(5L), Hobby(6L), Etc(7L);

    private final Long category;

    Category(Long category) {
        this.category = category;
    }

    public Long getCategoryId() {
        return this.category;
    }

    public static List<CategoryDto> getCategories() {
        List<CategoryDto> list = new ArrayList<>();
        for (Category c: Category.values()) {
            list.add(CategoryDto.builder().categoryId(c.getCategoryId()).category(c).build());
        }
        return list;
    }

    public static Category getCategory(Long categoryId){
        for(Category c : Category.values()){
            if(c.getCategoryId().equals(categoryId)){
                return c;
            }
        }
        throw  new InvalidCategoryException("Not Found Category ID");
    }
}
