package com.sollertia.habit.domain.preset.repository;

import com.sollertia.habit.domain.preset.entity.PreSet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreSetRepository extends JpaRepository<PreSet,Long> {
    List<PreSet> findAllByCategoryId(Long categoryID);
}
