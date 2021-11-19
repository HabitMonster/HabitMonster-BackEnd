package com.sollertia.habit.domain.preset.dto;


import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class PreSetResponseDto extends DefaultResponseDto {
    private List<PreSetVo> preSets;
}
