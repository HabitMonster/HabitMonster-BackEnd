package com.sollertia.habit.domain.avatar.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarSelectRequestDto {
   private Long avatarId;
   private String avatarName;
}
