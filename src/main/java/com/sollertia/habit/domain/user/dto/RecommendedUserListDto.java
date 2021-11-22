package com.sollertia.habit.domain.user.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class RecommendedUserListDto extends DefaultResponseDto {
    private List<RecommendationResponseVo> userList;
}
