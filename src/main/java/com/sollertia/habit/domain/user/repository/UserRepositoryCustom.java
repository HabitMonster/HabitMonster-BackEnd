package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> searchTop10ByCategory(Category category);
}
