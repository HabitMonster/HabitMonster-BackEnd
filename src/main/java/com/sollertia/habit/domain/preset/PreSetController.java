package com.sollertia.habit.domain.preset;

import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.enums.PreSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PreSetController {

    @GetMapping("/categories/{category_id}/presets")
    public PreSetResponseDto categoryPresetList(@PathVariable Long category_id){
            List<PreSetVo> list = PreSet.getPreSetList(category_id);
            return PreSetResponseDto.builder().preSets(list).statusCode(200).responseMessage("PreSets 전달 완료").build();
    }

    @GetMapping("/presets/{preset_id}")
    public PreSetResponseDto selectPreSet(@PathVariable Long preset_id){
        PreSetVo preSetVo = PreSet.getPreSet(preset_id);
        return PreSetResponseDto.builder().preSetVo(preSetVo).statusCode(200).responseMessage("PreSet 전달 완료").build();
    }
}
