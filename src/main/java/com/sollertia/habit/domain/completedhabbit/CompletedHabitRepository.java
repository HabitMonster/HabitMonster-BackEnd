package com.sollertia.habit.domain.completedhabbit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CompletedHabitRepository extends JpaRepository<CompletedHabit, Long> {
}
