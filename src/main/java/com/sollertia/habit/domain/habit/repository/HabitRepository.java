package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HabitRepository<T extends Habit> extends JpaRepository<T, Long> {
    @Query("select h from Habit h " +
            "where h.user = :user " +
            "and h.practiceDays like %:day% " +
            "and h.durationStart <= :today " +
            "and h.durationEnd >= :today " +
            "and h.isAccomplishInSession = false " +
            "order by h.createdAt desc")
    List<Habit> findTodayHabitListByUser(User user, int day, LocalDate today);
}
