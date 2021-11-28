package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HabitRepository<T extends Habit> extends JpaRepository<T, Long>, HabitRepositoryCustom{
    @Query("select h from Habit h " +
            "where h.user = :user " +
            "and h.practiceDays like %:day% " +
            "and h.durationStart <= :today " +
            "and h.durationEnd >= :today " +
            "order by h.isAccomplishInSession, h.createdAt desc")
    List<Habit> findTodayHabitListByUser(@Param("user") User user,@Param("day") int day,@Param("today") LocalDate today);

    List<Habit> findByUserOrderByCreatedAtDesc(User user);

    Integer countByUser(User user);
}
