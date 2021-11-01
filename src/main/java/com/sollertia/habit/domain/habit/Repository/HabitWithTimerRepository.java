package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.HabitWithTimer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithTimerRepository extends JpaRepository<HabitWithTimer, Long> {

}
