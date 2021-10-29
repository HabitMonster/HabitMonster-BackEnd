package com.sollertia.habit.domain.avatar.dto;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.Getter;

@Getter
public class AvatarResponseDto extends DefaultResponseDto {
    private Long avatarId;
    private String avatarImage;
    private String avatarName;

    private AvatarResponseDto(String responseMessage, Long avatarId, String avatarImage, String avatarName) {
        super(responseMessage);
        this.avatarId = avatarId;
        this.avatarImage = avatarImage;
        this.avatarName = avatarName;
    }

    public AvatarResponseDto(String responseMessage, Avatar avatar, String avatarName) {
        this(responseMessage, avatar.getId(), avatar.getImageUrl(), avatarName);
    }
}
