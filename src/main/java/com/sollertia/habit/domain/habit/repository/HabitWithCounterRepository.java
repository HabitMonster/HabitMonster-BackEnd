package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithCounterRepository extends JpaRepository<HabitWithCounter, Long> {
}
