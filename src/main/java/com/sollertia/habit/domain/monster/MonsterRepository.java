package com.sollertia.habit.domain.monster;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    List<Monster> findAllByGrade(EvolutionGrade ev1);
}
