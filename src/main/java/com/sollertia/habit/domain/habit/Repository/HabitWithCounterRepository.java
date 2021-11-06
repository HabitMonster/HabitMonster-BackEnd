package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitWithCounter;
import com.sollertia.habit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

public interface HabitWithCounterRepository extends JpaRepository<HabitWithCounter, Long> {
    List<Habit> findByUser(User user);

    List<Habit> findByUserAndPracticeDaysContains(User user, int day);

    @Query("select h from HabitWithCounter h " +
            "where h.user = :user " +
            "and h.practiceDays like %:day% " +
            "and h.durationStart <= :today " +
            "and h.durationEnd >= :today")
    List<Habit> findTodayHabitListByUser(User user, int day, LocalDate today);
}
