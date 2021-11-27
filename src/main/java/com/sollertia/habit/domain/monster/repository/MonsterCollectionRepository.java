package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterCollectionRepository extends JpaRepository<MonsterCollection, Long>,
        MonsterCollectionRepositoryCustom {
    MonsterCollection findByUserAndMonsterType(User user, MonsterType monsterType);
}
