package com.sollertia.habit.domain.completedhabbit.repository;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompletedHabitRepository extends JpaRepository<CompletedHabit, Long>, CompletedHabitRepositoryCustom {

    List<CompletedHabit> findAllByUserAndStartDateBetweenOrderByStartDate(User user, LocalDate start, LocalDate end);

    Integer countByUser(User user);

    void deleteByUser(User user);

    void deleteAllByUserIn(List<User> userList);
}
