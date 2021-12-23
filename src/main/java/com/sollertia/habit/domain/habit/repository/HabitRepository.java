package com.sollertia.habit.domain.habit.repository;

import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HabitRepository<T extends Habit> extends JpaRepository<T, Long>, HabitRepositoryCustom {
    @Query("select h from Habit h " +
            "where h.user = :user " +
            "and h.practiceDays like %:day% " +
            "and h.durationStart <= :today " +
            "and h.durationEnd >= :today " +
            "order by h.isAccomplishInPeriod, h.createdAt desc")
    List<Habit> findTodayHabitListByUser(@Param("user") User user, @Param("day") int day, @Param("today") LocalDate today);

    @Query("select h from Habit h join fetch h.user where h.practiceDays like %:day% and h.isAccomplishInPeriod = :complete")
    List<Habit> findHabitsWithDaysAndAccomplish(@Param("day") String day, @Param("complete") Boolean complete);


    @Modifying(clearAutomatically = true)
    @Query("update Habit h set h.isAccomplishInPeriod = false")
    int updateAccomplishInSessionToFalse();

    @Modifying(clearAutomatically = true)
    @Query("update HabitWithCounter h set h.currentCount = 0 where h.currentCount > 0")
    void updateCurrentCountZero();

    List<Habit> findAllByDurationEndLessThanEqual(LocalDate date);
    List<Habit> findByUser(User user);

    List<Habit> findByUserOrderByCreatedAtDesc(User user);

    Integer countByUser(User user);
}
