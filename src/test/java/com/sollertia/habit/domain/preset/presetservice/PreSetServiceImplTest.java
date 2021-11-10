package com.sollertia.habit.domain.preset.presetservice;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class PreSetServiceImplTest {

    @InjectMocks
    private PreSetServiceImpl preSetService;
    @MockBean
    private PreSetRepository preSetRepository;

    @DisplayName("카테고리별 프리셋 조회")
    @Test
    void categoryPreSetList(){
        //given
        PreSetVo preSetVo = PreSetVo.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
//        List<PreSetVo>preSetVoList = new ArrayList<>();
//        preSetVoList.add(preSetVo);
        List<PreSet> preSets = new ArrayList<>();
        preSets.add(new PreSet(preSetVo));
        given(preSetRepository.findAllByCategoryId(1L)).willReturn(preSets);
//        given(new PreSetVo(any(PreSet.class))).willReturn(preSetVo);
//        given(preSetRepository.findAllByCategoryId(anyLong()).stream().map(PreSetVo::new)
//                .collect(Collectors.toCollection(ArrayList::new))).willReturn(preSetVoList);

        //when
        List<PreSetVo> preSetVoListd= preSetService.categoryPreSetList(1L);

        //then
//        assertThat(preSetVoList.get(0).getPresetId()).isEqualTo(preSets.get(0).getId());
//        assertThat(preSetVoList.get(0).getCategoryId()).isEqualTo(preSets.get(0).getCategoryId());
//        assertThat(preSetVoList.get(0).getTitle()).isEqualTo(preSets.get(0).getTitle());
//        assertThat(preSetVoList.get(0).getDescription()).isEqualTo(preSets.get(0).getDescription());
//        assertThat(preSetVoList.get(0).getPeriod()).isEqualTo(preSets.get(0).getPeriod());
//        assertThat(preSetVoList.get(0).getCount()).isEqualTo(preSets.get(0).getCount());
//        assertThat(preSetVoList.get(0).getCategory()).isEqualTo(preSets.get(0).getCategory());
//        assertThat(preSetVoList.get(0).getPracticeDays()).isEqualTo(preSets.get(0).getPracticeDays());

        //verify(preSetRepository).findAllByCategoryId(anyLong());

    }




}