package com.sollertia.habit.domain.preset.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PreSetResponseDto implements Serializable {
    private List<PreSetDto> preSets;
    private Integer statusCode;
    private String responseMessage;
}
