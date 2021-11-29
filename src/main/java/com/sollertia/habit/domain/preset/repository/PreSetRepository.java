package com.sollertia.habit.domain.preset.repository;

import com.sollertia.habit.domain.preset.entity.PreSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreSetRepository extends JpaRepository<PreSet,Long> {
    List<PreSet> findAllByCategoryId(Long categoryID);
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM PreSet where userId is not null")
    void deletePresetByScheduler();
}
