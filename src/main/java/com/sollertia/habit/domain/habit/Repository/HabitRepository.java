package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
