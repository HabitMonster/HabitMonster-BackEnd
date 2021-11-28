package com.sollertia.habit.global.scheduler.repository;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.global.scheduler.entity.CategoryAvg;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryAvgRepository extends JpaRepository<CategoryAvg, Long> {
    CategoryAvg findByCategory(Category category);
}
