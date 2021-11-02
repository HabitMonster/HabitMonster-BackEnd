package com.sollertia.habit.domain.habit.Repository;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitWithCounter;
import com.sollertia.habit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;

public interface HabitWithCounterRepository extends JpaRepository<HabitWithCounter, Long> {
    List<Habit> findByUser(User user);

    List<Habit> findByUserAndPracticeDaysContains(User user, int day);
}
