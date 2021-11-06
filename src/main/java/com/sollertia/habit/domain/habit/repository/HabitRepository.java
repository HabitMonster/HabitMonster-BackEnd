package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.Habit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HabitRepository<T extends Habit> extends JpaRepository<T, Long> {
    @Query("select h from Habit h where h.practiceDays like %:day% and h.isAccomplishInSession = :complete")
    List<Habit> findHabitsWithDaysAndAccomplish(@Param("day") String day, @Param("complete") Boolean complete);

    @Modifying(clearAutomatically = true)
    @Query("update Habit h set h.isAccomplishInSession = false")
    int updateAccomplishInSession();

}
