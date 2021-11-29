package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.entity.Recommendation;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long>, RecommendationRepositoryCustom {
    void deleteByUser(User user);
}
