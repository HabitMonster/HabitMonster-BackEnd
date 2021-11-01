package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.HabitWithCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithCounterRepository extends JpaRepository<HabitWithCounter, Long> {
}
