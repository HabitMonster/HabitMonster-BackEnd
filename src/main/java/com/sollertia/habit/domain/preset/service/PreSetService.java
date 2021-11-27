package com.sollertia.habit.domain.preset.service;

import com.sollertia.habit.domain.preset.dto.PreSetVo;

import java.util.List;

public interface PreSetService {
    List<PreSetDto> categoryPreSetList(Long categoryId);
    PreSetDto getPreSet(Long preSetId);
    void deletePreSet();
}
