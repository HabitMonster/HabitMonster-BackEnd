package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class RecommendedUserListDto extends DefaultResponseDto {
    private List<RecommendationResponseVo> userList;
}
