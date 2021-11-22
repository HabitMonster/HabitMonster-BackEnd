package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseVo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendationResponseVo {
    private String title;
    private FollowSearchResponseVo userInfo;
}
