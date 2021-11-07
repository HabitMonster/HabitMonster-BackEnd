package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.HabitWithTimer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitWithTimerRepository extends JpaRepository<HabitWithTimer, Long> {

}
