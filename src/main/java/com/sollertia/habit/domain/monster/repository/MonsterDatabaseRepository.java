package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterDatabaseRepository extends JpaRepository<MonsterDatabase, Long> {
    List<MonsterDatabase> findAllByGrade(EvolutionGrade ev1);
}
