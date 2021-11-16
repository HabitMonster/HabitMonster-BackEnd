package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class FollowResponseDto extends DefaultResponseDto {
    List<FollowVo> followers;
    List<FollowVo> followings;
}
