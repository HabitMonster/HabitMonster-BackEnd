package com.sollertia.habit.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;
import lombok.Getter;

@Getter
public class RecommendationDto {
    private String title;
    private FollowDto userInfo;

    @QueryProjection
    public RecommendationDto(RecommendationType type, FollowDto userInfo) {
        this.title = type.getTitle();
        this.userInfo = userInfo;
    }
}
