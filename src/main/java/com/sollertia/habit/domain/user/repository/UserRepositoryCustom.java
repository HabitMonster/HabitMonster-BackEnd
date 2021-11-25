package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.dto.UserMonsterVo;
import com.sollertia.habit.domain.user.entity.User;

public interface UserRepositoryCustom {
    UserMonsterVo userDetailByMonsterCode(String monsterCode, User login);
}
