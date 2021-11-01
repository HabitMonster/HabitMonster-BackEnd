package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.enums.PreSet;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@RestController
public class PreSetController {

    @ApiOperation(value = "선택한 Category의 PreSet 목록 조회")
    @PostMapping("/categories/{category_id}/presets")
    public PreSetResponseDto categoryPresetList(@PathVariable Long category_id){
            List<PreSetVo> list = PreSet.getPreSetList(category_id);
            return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets 전달 완료").build();
    }

}
