package com.sollertia.habit.domain.preset.presetservice;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.global.exception.preset.PreSetNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class PreSetServiceImplTest {

    @InjectMocks
    private PreSetServiceImpl preSetService;
    @Mock
    private PreSetRepository preSetRepository;

    @DisplayName("카테고리별 프리셋 조회")
    @Test
    void categoryPreSetList(){
        //given
        PreSetVo preSetVo = PreSetVo.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
        List<PreSet> preSets = new ArrayList<>();
        preSets.add(new PreSet(preSetVo));
        given(preSetRepository.findAllByCategoryId(1L)).willReturn(preSets);

        //when
        List<PreSetVo> preSetVoList= preSetService.categoryPreSetList(1L);

        //then
        assertThat(preSetVoList.get(0).getPresetId()).isEqualTo(preSets.get(0).getId());
        assertThat(preSetVoList.get(0).getCategoryId()).isEqualTo(preSets.get(0).getCategoryId());
        assertThat(preSetVoList.get(0).getTitle()).isEqualTo(preSets.get(0).getTitle());
        assertThat(preSetVoList.get(0).getDescription()).isEqualTo(preSets.get(0).getDescription());
        assertThat(preSetVoList.get(0).getPeriod()).isEqualTo(preSets.get(0).getPeriod());
        assertThat(preSetVoList.get(0).getCount()).isEqualTo(preSets.get(0).getCount());
        assertThat(preSetVoList.get(0).getCategory()).isEqualTo(preSets.get(0).getCategory());
        assertThat(preSetVoList.get(0).getPracticeDays()).isEqualTo(preSets.get(0).getPracticeDays());

        verify(preSetRepository).findAllByCategoryId(1L);

    }

    @DisplayName("프리셋 조회")
    @Test
    void getPreSet(){
        //given
        PreSetVo preSetVo = PreSetVo.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
        given(preSetRepository.findById(1L)).willReturn(Optional.of(new PreSet(preSetVo)));

        //when
        PreSetVo preSetVoRes = preSetService.getPreSet(1L);

        //then
        assertThat(preSetVoRes.getCategoryId()).isEqualTo(preSetVo.getCategoryId());
        assertThat(preSetVoRes.getTitle()).isEqualTo(preSetVo.getTitle());
        assertThat(preSetVoRes.getDescription()).isEqualTo(preSetVo.getDescription());
        assertThat(preSetVoRes.getPeriod()).isEqualTo(preSetVo.getPeriod());
        assertThat(preSetVoRes.getCount()).isEqualTo(preSetVo.getCount());
        assertThat(preSetVoRes.getCategory()).isEqualTo(preSetVo.getCategory());
        assertThat(preSetVoRes.getPracticeDays()).isEqualTo(preSetVo.getPracticeDays());

        verify(preSetRepository).findById(1L);

    }


}