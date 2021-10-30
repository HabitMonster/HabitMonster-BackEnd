package com.sollertia.habit.domain.preset.dto;

import com.sollertia.habit.domain.preset.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class PreSetVo {
    private Long presetId;
    private Long categoryId;
    private String title;
    private String description;
    private int period;
    private Long count;
    private Category category;
    private String session;
    private List<Integer> week;
}
