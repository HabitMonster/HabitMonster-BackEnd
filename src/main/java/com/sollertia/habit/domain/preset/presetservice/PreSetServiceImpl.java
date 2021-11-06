package com.sollertia.habit.domain.preset.presetservice;


import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PreSetServiceImpl implements PreSetService{

    private final PreSetRepository preSetRepository;

    @Override
    public  List<PreSetVo> categoryPreSetList(Long categoryId) {
        return preSetRepository.findAllByCategoryId(categoryId).stream().map(PreSetVo::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public PreSetVo getPreSet(Long preSetId) {
        return preSetRepository.findById(preSetId).map(PreSetVo::new).orElseThrow(()->new IllegalArgumentException("PreSet없음"));
    }

    @Transactional
    @Override
    public void deletePreSet() {
        try {
            preSetRepository.deletePresetBySchduler();
        }catch (Exception e){
            throw new IllegalArgumentException("삭제할 PreSet 없습니다");
        }
    }
}
