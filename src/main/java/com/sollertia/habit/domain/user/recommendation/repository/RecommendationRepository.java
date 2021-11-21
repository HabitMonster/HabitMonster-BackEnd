package com.sollertia.habit.domain.user.recommendation.repository;

import com.sollertia.habit.domain.user.recommendation.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
}
