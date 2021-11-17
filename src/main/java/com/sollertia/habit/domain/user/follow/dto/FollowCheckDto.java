package com.sollertia.habit.domain.user.follow.dto;

import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FollowCheckDto extends DefaultResponseDto {
    private Boolean isFollowed;
}
