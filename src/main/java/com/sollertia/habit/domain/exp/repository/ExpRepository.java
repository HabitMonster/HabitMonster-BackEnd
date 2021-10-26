package com.sollertia.habit.domain.exp.repository;

import com.sollertia.habit.domain.exp.Exp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpRepository extends JpaRepository<Exp, Long> {
}
