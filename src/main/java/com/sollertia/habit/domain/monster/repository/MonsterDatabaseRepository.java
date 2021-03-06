package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonsterDatabaseRepository extends JpaRepository<MonsterDatabase, Long> {
    List<MonsterDatabase> findAllByLevel(Level level);

    Optional<MonsterDatabase> findByMonsterTypeAndLevel(MonsterType monsterType, Level level);
}
