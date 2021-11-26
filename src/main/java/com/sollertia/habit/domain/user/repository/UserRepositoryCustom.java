package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.dto.UserMonsterDto;
import com.sollertia.habit.domain.user.entity.User;

public interface UserRepositoryCustom {
    UserMonsterDto userDetailByMonsterCode(String monsterCode, User login);
}
