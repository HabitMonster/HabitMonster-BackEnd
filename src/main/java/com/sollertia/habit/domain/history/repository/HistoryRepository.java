package com.sollertia.habit.domain.history.repository;

import com.sollertia.habit.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long>, HistoryRepositoryCustom {
}
