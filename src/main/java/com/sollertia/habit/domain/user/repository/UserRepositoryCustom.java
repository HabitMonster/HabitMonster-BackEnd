package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.user.dto.UserMonsterDto;
import com.sollertia.habit.domain.user.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> searchTop10ByCategory(Category category);

    List<User> searchTop10ByFollow();
    UserMonsterDto userDetailByMonsterCode(String monsterCode, User login);
}
