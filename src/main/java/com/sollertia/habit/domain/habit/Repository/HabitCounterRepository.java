package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HabitCounterRepository<T extends HabitWithCounter> extends JpaRepository<T, Long> {
    List<T> findAllByUserId(Long userId);
}
