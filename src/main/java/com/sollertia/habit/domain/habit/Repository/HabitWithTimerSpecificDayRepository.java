package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimerSpecificDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithTimerSpecificDayRepository extends JpaRepository<HabitWithTimerSpecificDay, Long> {

}
