package com.sollertia.habit.domain.preset.service;


import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.global.exception.preset.PreSetNotFoundException;
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
    public  List<PreSetDto> categoryPreSetList(Long categoryId) {
            return preSetRepository.findAllByCategoryId(categoryId).stream().map(PreSetDto::new)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public PreSetDto getPreSet(Long preSetId) {
        return preSetRepository.findById(preSetId).map(PreSetDto::new).orElseThrow(()->new PreSetNotFoundException("Not Found PreSet"));
    }

    @Transactional
    @Override
    public void deletePreSet() {
        try {
            preSetRepository.deletePresetByScheduler();
        } catch (Exception e) {
            throw new IllegalArgumentException("Not Found PreSet");
        }
    }
}
