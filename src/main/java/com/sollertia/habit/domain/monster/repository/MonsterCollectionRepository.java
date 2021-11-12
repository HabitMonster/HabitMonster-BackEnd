package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.dto.MonsterCollectionVo;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MonsterCollectionRepository extends JpaRepository<MonsterCollection, Long> {
    List<MonsterCollection> findAllByUser(User user);

    List<MonsterCollection> findVoListByUser(User user);

    MonsterCollection findByUserAndMonsterType(User user, MonsterType monsterType);
}
