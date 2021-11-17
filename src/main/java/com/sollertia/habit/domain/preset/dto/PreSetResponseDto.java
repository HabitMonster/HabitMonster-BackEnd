package com.sollertia.habit.domain.preset.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class PreSetResponseDto extends DefaultResponseDto {
    private List<PreSetVo> preSets;
}
