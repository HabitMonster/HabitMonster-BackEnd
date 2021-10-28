package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitCounterRepository<T extends HabitWithCounter> extends JpaRepository<T, Long> {
}
