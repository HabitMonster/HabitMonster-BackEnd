package com.sollertia.habit.domain.habbit.Repository;

import com.sollertia.habit.domain.habbit.HabbitWithCounter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabbitWithCounterRepository extends JpaRepository<HabbitWithCounter, Long> {
}
