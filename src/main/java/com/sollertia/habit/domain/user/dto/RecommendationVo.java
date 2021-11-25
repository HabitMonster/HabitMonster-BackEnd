package com.sollertia.habit.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseVo;
import lombok.Getter;

@Getter
public class RecommendationVo {
    private String title;
    private FollowSearchResponseVo userInfo;

    @QueryProjection
    public RecommendationVo(String title, FollowSearchResponseVo userInfo) {
        this.title = title;
        this.userInfo = userInfo;
    }
}
