package com.sollertia.habit.domain.avatar.dto;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.utils.DefaultResponseDto;

import java.util.List;

public class AvatarListResponseDto extends DefaultResponseDto {
    private List<Avatar> avatars;

    public AvatarListResponseDto(String responseMessage, List<Avatar> avatars) {
        super(responseMessage);
        this.avatars = avatars;
    }
}
