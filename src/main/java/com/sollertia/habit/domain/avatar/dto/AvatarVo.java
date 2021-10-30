package com.sollertia.habit.domain.avatar.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AvatarVo {
    private Long avatarid;
    private String avatarImage;
    private String avatarName;
}
