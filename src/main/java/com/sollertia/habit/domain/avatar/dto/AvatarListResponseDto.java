package com.sollertia.habit.domain.avatar.dto;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class AvatarListResponseDto extends DefaultResponseDto {
    private List<Avatar> avatars;
}
