package com.sollertia.habit.domain.avatar.dto;

import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AvatarResponseDto extends DefaultResponseDto {
    private Long avatarId;
    private String avatarName;
}
