package com.sollertia.habit.domain.completedhabbit;

import com.sollertia.habit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompletedHabitRepository extends JpaRepository<CompletedHabit, Long> {

    List<CompletedHabit> findAllByUserAndCreatedAtBetween(User user, LocalDate start, LocalDate end);

    List<CompletedHabit> findAllByUserAndIsSuccessTrueAndCreatedAtBetween(User user, LocalDate start, LocalDate end);

    List<CompletedHabit> findAllByUserAndIsSuccessFalseAndCreatedAtBetween(User user, LocalDate start, LocalDate end);
}
