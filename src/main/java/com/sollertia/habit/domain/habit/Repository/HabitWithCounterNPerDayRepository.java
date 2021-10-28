package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounterNPerDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithCounterNPerDayRepository extends JpaRepository<HabitWithCounterNPerDay, Long> {
}
