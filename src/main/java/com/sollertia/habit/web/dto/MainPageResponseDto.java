package com.sollertia.habit.web.dto;

import com.sollertia.habit.domain.avatar.dto.AvatarResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitSummaryResponseDto;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class MainPageResponseDto extends DefaultResponseDto {
    private AvatarResponseDto avatar;
    private Long expPercentage;
    private List<HabitSummaryResponseDto> habits;
}
