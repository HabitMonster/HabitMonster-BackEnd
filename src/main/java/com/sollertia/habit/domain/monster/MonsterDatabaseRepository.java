package com.sollertia.habit.domain.monster;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterDatabaseRepository extends JpaRepository<MonsterDatabase, Long> {
    List<MonsterDatabase> findAllByGrade(EvolutionGrade ev1);
}
