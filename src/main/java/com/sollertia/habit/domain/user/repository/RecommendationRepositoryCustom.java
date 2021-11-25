package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.dto.RecommendationVo;
import com.sollertia.habit.domain.user.entity.User;

import java.util.List;

public interface RecommendationRepositoryCustom {
    List<RecommendationVo> searchByNumber(User login, int number);
}
