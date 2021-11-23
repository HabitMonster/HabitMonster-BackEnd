package com.sollertia.habit.domain.user.enums;

import com.sollertia.habit.global.exception.user.InvalidRecommendationTypeException;

import java.util.Arrays;

public enum RecommendationType {
    HEALTH_TOP10(1L, "건강 카테고리 습관 추천"),
    STUDY_TOP10(2L, "공부 카테고리 습관 추천"),
    LIFE_TOP10(3L, "생활 카테고리 습관 추천"),
    EMOTION_TOP10(4L, "감정 카테고리 습관 추천"),
    RELATION_TOP10(5L, "관계 카테고리 습관 추천"),
    HOBBY_TOP10(6L, "취미 카테고리 습관 추천"),
    ETC_TOP10(7L, "기타 카테고리 습관 추천"),
    CREATE_TOP10(8L, "많은 습관 성공"),
    FOLLOWERS_TOP10(9L, "많은 팔로워 수");

    private final Long id;
    private final String title;

    RecommendationType(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    public static RecommendationType from(Long id) {
        return Arrays.stream(RecommendationType.values())
                .filter(recommendationType -> recommendationType.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new InvalidRecommendationTypeException("Not Found RecommendationType ID"));
    }
}
