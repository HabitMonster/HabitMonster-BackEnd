package com.sollertia.habit.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;
import lombok.Getter;

@Getter
public class RecommendationVo {
    private String title;
    private FollowVo userInfo;

    @QueryProjection
    public RecommendationVo(RecommendationType type, FollowVo userInfo) {
        this.title = type.getTitle();
        this.userInfo = userInfo;
    }
}
