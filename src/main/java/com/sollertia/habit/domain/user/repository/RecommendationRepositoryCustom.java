package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.dto.RecommendationDto;
import com.sollertia.habit.domain.user.entity.User;

import java.util.List;

public interface RecommendationRepositoryCustom {
    List<RecommendationDto> searchByNumber(User login, int number);
}
