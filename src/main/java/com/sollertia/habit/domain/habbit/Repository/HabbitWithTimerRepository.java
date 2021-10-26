package com.sollertia.habit.domain.habbit.Repository;

import com.sollertia.habit.domain.habbit.HabbitWithTimer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabbitWithTimerRepository extends JpaRepository<HabbitWithTimer, Long> {
}
