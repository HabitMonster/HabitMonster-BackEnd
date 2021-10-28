package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounterSpecificDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithCounterSpecificDayRepository extends JpaRepository<HabitWithCounterSpecificDay, Long> {

}
