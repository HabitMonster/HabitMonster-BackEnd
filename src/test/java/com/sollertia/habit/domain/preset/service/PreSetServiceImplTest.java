package com.sollertia.habit.domain.preset.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
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
        PreSetDto preSetDto = PreSetDto.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
        List<PreSet> preSets = new ArrayList<>();
        preSets.add(new PreSet(preSetDto));
        given(preSetRepository.findAllByCategoryId(1L)).willReturn(preSets);

        //when
        List<PreSetDto> preSetDtoList = preSetService.categoryPreSetList(1L);

        //then
        assertThat(preSetDtoList.get(0).getPresetId()).isEqualTo(preSets.get(0).getId());
        assertThat(preSetDtoList.get(0).getCategoryId()).isEqualTo(preSets.get(0).getCategoryId());
        assertThat(preSetDtoList.get(0).getTitle()).isEqualTo(preSets.get(0).getTitle());
        assertThat(preSetDtoList.get(0).getDescription()).isEqualTo(preSets.get(0).getDescription());
        assertThat(preSetDtoList.get(0).getPeriod()).isEqualTo(preSets.get(0).getPeriod());
        assertThat(preSetDtoList.get(0).getCount()).isEqualTo(preSets.get(0).getCount());
        assertThat(preSetDtoList.get(0).getCategory()).isEqualTo(preSets.get(0).getCategory());
        assertThat(preSetDtoList.get(0).getPracticeDays()).isEqualTo(preSets.get(0).getPracticeDays());

        verify(preSetRepository).findAllByCategoryId(1L);

    }

    @DisplayName("프리셋 조회")
    @Test
    void getPreSet(){
        //given
        PreSetDto preSetDto = PreSetDto.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
        given(preSetRepository.findById(1L)).willReturn(Optional.of(new PreSet(preSetDto)));

        //when
        PreSetDto preSetDtoRes = preSetService.getPreSet(1L);

        //then
        assertThat(preSetDtoRes.getCategoryId()).isEqualTo(preSetDto.getCategoryId());
        assertThat(preSetDtoRes.getTitle()).isEqualTo(preSetDto.getTitle());
        assertThat(preSetDtoRes.getDescription()).isEqualTo(preSetDto.getDescription());
        assertThat(preSetDtoRes.getPeriod()).isEqualTo(preSetDto.getPeriod());
        assertThat(preSetDtoRes.getCount()).isEqualTo(preSetDto.getCount());
        assertThat(preSetDtoRes.getCategory()).isEqualTo(preSetDto.getCategory());
        assertThat(preSetDtoRes.getPracticeDays()).isEqualTo(preSetDto.getPracticeDays());

        verify(preSetRepository).findById(1L);

    }


}