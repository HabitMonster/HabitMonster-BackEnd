package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.enums.PreSet;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
public class PreSetController {

    @ApiOperation(value = "선택한 Category의 PreSet 목록 조회")
    @GetMapping("/categories/{category_id}/presets")
    public PreSetResponseDto categoryPresetList(@PathVariable Long category_id){
            List<PreSetVo> list = PreSet.getPreSetList(category_id);
            return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets 전달 완료").build();
    }

    @ApiOperation(value = "선택한 PreSet Habit 테이블에 저장")
    @GetMapping("/presets/{preset_id}")
    public PreSetResponseDto selectPreSet(@PathVariable Long preset_id){
        PreSetVo preSetVo = PreSet.getPreSet(preset_id);
        assert preSetVo != null;

        Calendar Startdate = Calendar.getInstance();
        Calendar Enddate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        Enddate.add(Calendar.DATE, preSetVo.getPeriod());

        HabitDtoImpl habitDto = HabitDtoImpl.builder().title(preSetVo.getTitle()).description(preSetVo.getDescription()).
        durationStart(form.format(Startdate.getTime())).durationEnd(form.format(Enddate.getTime())).categoryType(preSetVo.getCategory()).
        count(preSetVo.getCount()).sessionDuration(1L).week(preSetVo.getWeek()).build();
        return PreSetResponseDto.builder().habitDto(habitDto).statusCode(200).responseMessage("PreSet 저장 완료").build();
    }
}
