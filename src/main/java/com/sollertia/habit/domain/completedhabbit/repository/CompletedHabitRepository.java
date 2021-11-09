package com.sollertia.habit.domain.completedhabbit.repository;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CompletedHabitRepository extends JpaRepository<CompletedHabit, Long> {

    List<CompletedHabit> findAllByUserAndCreatedAtBetween(User user, LocalDate start, LocalDate end);

    List<CompletedHabit> findAllByUserAndStartDateBetweenOrderByStartDate(User user, LocalDate start, LocalDate end);
    @Query(value = "select c.user.id, count(c.user) as cnt from CompletedHabit c group by c.user, c.isSuccess having c.isSuccess = true order by cnt desc")
    Long maxIsSuccessTrueUser(PageRequest pageRequest);
}
