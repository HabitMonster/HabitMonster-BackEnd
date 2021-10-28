package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimerNPerDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithTimerNPerDayRepository extends JpaRepository<HabitWithTimerNPerDay, Long> {
}
