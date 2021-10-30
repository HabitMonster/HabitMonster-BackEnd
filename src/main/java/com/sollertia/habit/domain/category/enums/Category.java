package com.sollertia.habit.domain.category.enums;

import com.sollertia.habit.domain.category.dto.CategoryVo;

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

    public static List<CategoryVo> getCategories() {
        List<CategoryVo> list = new ArrayList<>();
        for (Category p : Category.values()) {
            list.add(CategoryVo.builder().categoryId(p.getCategoryId()).category(p).build());
        }
        return list;
    }
}
