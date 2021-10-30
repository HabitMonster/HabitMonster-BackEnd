package com.sollertia.habit.domain.avatar.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AvatarVo {
    private Long avatarid;
    private String avatarImage;
    private String avatarName;
}
