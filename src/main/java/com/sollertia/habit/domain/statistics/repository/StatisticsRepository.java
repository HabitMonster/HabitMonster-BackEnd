package com.sollertia.habit.domain.statistics.repository;

import com.sollertia.habit.domain.statistics.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
}
