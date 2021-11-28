package com.sollertia.habit.domain.statistics.repository;

import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    @Modifying(clearAutomatically = true)
    @Query("delete from Statistics h where h.sessionType = :sessionType")
    void deleteAllBySessionType(@Param("sessionType") SessionType sessionType);
}
