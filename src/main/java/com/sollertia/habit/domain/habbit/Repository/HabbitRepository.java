package com.sollertia.habit.domain.habbit.Repository;

import com.sollertia.habit.domain.habbit.Habbit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabbitRepository extends JpaRepository<Habbit, Long> {
}
