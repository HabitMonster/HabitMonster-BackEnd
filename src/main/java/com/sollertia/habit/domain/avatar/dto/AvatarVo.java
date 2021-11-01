package com.sollertia.habit.domain.avatar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@AllArgsConstructor
public class AvatarVo {
    private Long avatarId;
    private String avatarImage;
    private String avatarName;
}
