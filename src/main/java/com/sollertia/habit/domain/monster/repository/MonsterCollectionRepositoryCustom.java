package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;

import java.util.List;

public interface MonsterCollectionRepositoryCustom {
    List<MonsterCollection> searchByUser(User user);

    List<MonsterType> searchTypeListByUser(User user);
}
