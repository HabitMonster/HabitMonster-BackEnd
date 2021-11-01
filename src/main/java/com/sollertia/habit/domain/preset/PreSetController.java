package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.presetservice.PreSetServiceImpl;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.domain.habit.habitservice.HabitServiceImpl;
import com.sollertia.habit.utils.DefaultResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
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

    @ApiOperation(value = "선택한 Category의 PreSet 목록 조회")
    @PostMapping("/categories/{category_id}/presets")
    public PreSetResponseDto categoryPresetList(@PathVariable Long category_id){
//            List<PreSetVo> list = PreSet.getPreSetList(category_id);
//            return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets 전달 완료").build();
        List<PreSetVo> list = preSetService.categoryPreSetList(category_id);
        return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets 전달 완료").build();
    }

    @ApiOperation(value = "선택한 PreSet Habit 테이블에 저장")
    @GetMapping("/presets/{preset_id}")
    public DefaultResponseDto selectPreSet(@PathVariable Long preset_id, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        PreSetVo preSetVo = PreSet.getPreSet(preset_id);
        PreSetVo preSetVo = preSetService.getPreSet(preset_id);

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");
        endDate.add(Calendar.DATE, preSetVo.getPeriod());

        HabitDtoImpl habitDto = HabitDtoImpl.builder().durationStart(form.format(startDate.getTime())).durationEnd(form.format(endDate.getTime()))
                .count(preSetVo.getCount()).title(preSetVo.getTitle()).description(preSetVo.getDescription()).practiseDays(preSetVo.getPractiseDays()).build();

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specifi cDay");

        habitDto.setUser(userDetails.getUser());
        habitService.createHabit(habitTypeDto, habitDto);

        return DefaultResponseDto.builder().responseMessage("습관 등록 완료").statusCode(200).build();
    }
}
