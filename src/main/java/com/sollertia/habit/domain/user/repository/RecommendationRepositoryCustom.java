package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.entity.Recommendation;

import java.util.List;

public interface RecommendationRepositoryCustom {
    List<Recommendation> searchByNumber(int number);
}
