package com.sollertia.habit.domain.preset.presetservice;

import com.sollertia.habit.domain.preset.dto.PreSetVo;

import java.util.List;

public interface PreSetService {
    List<PreSetVo> categoryPreSetList(Long categoryId);
    PreSetVo getPreSet(Long preSetId);
    void deletePreSet();
}
