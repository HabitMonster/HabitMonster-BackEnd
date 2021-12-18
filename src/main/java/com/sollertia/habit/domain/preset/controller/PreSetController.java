package com.sollertia.habit.domain.preset.controller;


import com.sollertia.habit.domain.habit.dto.HabitDetailResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.service.PreSetServiceImpl;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class PreSetController {

    private final PreSetServiceImpl preSetService;
    private final HabitServiceImpl habitService;

    @Cacheable(cacheNames = "PreSet", key = "#category_id")
    @ApiOperation(value = "선택한 Category의 PreSet 목록 조회")
    @GetMapping("/categories/{category_id}/presets")
    public PreSetResponseDto categoryPreSetList(@PathVariable Long category_id){
        List<PreSetDto> list = preSetService.categoryPreSetList(category_id);
        return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets Query Completed").build();
    }

    @ApiOperation(value = "선택한 PreSet Habit 테이블에 저장")
    @PostMapping("/presets/{preset_id}")
    public HabitDetailResponseDto selectPreSet(@PathVariable Long preset_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
        PreSetDto preSetDto = preSetService.getPreSet(preset_id);

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        endDate.add(Calendar.DATE, preSetDto.getPeriod());

        HabitDtoImpl habitDto = HabitDtoImpl.builder()
                .durationStart(form.format(startDate.getTime()))
                .durationEnd(form.format(endDate.getTime()))
                .count(preSetDto.getCount())
                .title(preSetDto.getTitle())
                .description(preSetDto.getDescription())
                .practiceDays(preSetDto.getPracticeDays())
                .categoryId(preSetDto.getCategoryId())
                .build();

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.createHabit(habitTypeDto, habitDto, userDetails.getUser());
    }
}
