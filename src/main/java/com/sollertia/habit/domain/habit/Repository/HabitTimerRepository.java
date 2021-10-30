package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitTimerRepository<T extends HabitWithTimer> extends JpaRepository<T, Long> {
    List<T> findAllByUserId(Long userId);
}
