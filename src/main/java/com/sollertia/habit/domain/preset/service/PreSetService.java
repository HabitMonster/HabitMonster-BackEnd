package com.sollertia.habit.domain.preset.service;

import com.sollertia.habit.domain.preset.dto.PreSetDto;

import java.util.List;

public interface PreSetService {
    List<PreSetDto> categoryPreSetList(Long categoryId);
    PreSetDto getPreSet(Long preSetId);
}
