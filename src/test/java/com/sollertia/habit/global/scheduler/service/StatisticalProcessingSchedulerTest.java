package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import com.sollertia.habit.global.scheduler.repository.CategoryAvgRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StatisticalProcessingSchedulerTest {

    @InjectMocks
    private StatisticalProcessingScheduler statisticalProcessingScheduler;

    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private CompletedHabitRepository completedHabitRepository;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private CategoryAvgRepository categoryAvgRepository;

    @Test
    void statisticsMonthMaxMinusByCategory() {
        //given
        SessionType monthly = SessionType.MONTHLY;
        List<StatisticsCategoryVo> categoryVoList = new ArrayList<>();
        StatisticsCategoryVo statisticsCategoryVo = new StatisticsCategoryVo();
        Whitebox.setInternalState(statisticsCategoryVo, "category", Category.Etc);
        Whitebox.setInternalState(statisticsCategoryVo, "num", 30L);
        categoryVoList.add(statisticsCategoryVo);

        given(historyRepository.statisticsMonthMaxMinusByCategory(any()))
                .willReturn(categoryVoList);

        //when
        statisticalProcessingScheduler.statisticsMonthMaxMinusByCategory(monthly);

        //then
        verify(historyRepository).statisticsMonthMaxMinusByCategory(any());
        verify(statisticsRepository).save(any());
    }

    @Test
    void statisticsAvgAchievementPercentageByCategory() {
        //given
        SessionType monthly = SessionType.MONTHLY;
        List<StatisticsSuccessCategoryAvgVo> list = new ArrayList<>();
        StatisticsSuccessCategoryAvgVo categoryAvgVo = new StatisticsSuccessCategoryAvgVo();
        Whitebox.setInternalState(categoryAvgVo, "category", Category.Relation);
        Whitebox.setInternalState(categoryAvgVo, "avgPer", 35d);
        list.add(categoryAvgVo);
        given(completedHabitRepository.statisticsAvgAchievementPercentageByCategory(any()))
                .willReturn(list);

        //when
        statisticalProcessingScheduler.statisticsAvgAchievementPercentageByCategory(monthly);

        //then
        verify(completedHabitRepository).statisticsAvgAchievementPercentageByCategory(any());
        verify(statisticsRepository).saveAll(any());
    }

    @Test
    void statisticsMaxSelectedByCategory() {
        //given
        SessionType monthly = SessionType.MONTHLY;
        List<StatisticsCategoryVo> categoryVoList = new ArrayList<>();
        StatisticsCategoryVo statisticsCategoryVo = new StatisticsCategoryVo();
        Whitebox.setInternalState(statisticsCategoryVo, "category", Category.Health);
        Whitebox.setInternalState(statisticsCategoryVo, "num", 30L);
        categoryVoList.add(statisticsCategoryVo);

        given(completedHabitRepository.statisticsMaxSelectedByCategory(any(), any()))
                .willReturn(categoryVoList);
        given(habitRepository.statisticsMaxSelectedByCategory(any()))
                .willReturn(categoryVoList);

        //when
        statisticalProcessingScheduler.statisticsMaxSelectedByCategory(monthly);

        //then
        verify(completedHabitRepository).statisticsMaxSelectedByCategory(any(), any());
        verify(habitRepository).statisticsMaxSelectedByCategory(any());
        verify(statisticsRepository).save(any());
    }

    @Test
    void maintainNumberOfHabitByUser() {
        //given
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);
        users.add(3L);
        users.add(4L);
        given(habitRepository.count())
                .willReturn(100L);
        given(habitRepository.statisticsGetNumberOfUser())
                .willReturn(users);

        //when
        statisticalProcessingScheduler.maintainNumberOfHabitByUser();

        //then
        verify(habitRepository).count();
        verify(habitRepository).statisticsGetNumberOfUser();
        verify(statisticsRepository).save(any());
    }

    @Test
    void saveMonsterTypeStatistics() {
        //given
        SessionType monthly = SessionType.WEEKLY;
        Map<MonsterType, Long> monsterTypeCount = new HashMap<>();
        monsterTypeCount.put(MonsterType.GREEN, 1L);
        monsterTypeCount.put(MonsterType.PINK, 2L);
        monsterTypeCount.put(MonsterType.BLUE, 3L);
        monsterTypeCount.put(MonsterType.YELLOW, 4L);
        monsterTypeCount.put(MonsterType.ORANGE, 5L);

        given(monsterRepository.getMonsterTypeCount(any()))
                .willReturn(monsterTypeCount);

        //when
        statisticalProcessingScheduler.saveMonsterTypeStatistics(monthly);

        //then
        verify(monsterRepository).getMonsterTypeCount(any());
        verify(statisticsRepository, times(2)).save(any());
    }

    @Test
    void saveMostFailedDay() {
        //given
        SessionType monthly = SessionType.WEEKLY;
        Map<Integer, Long> mostFailedDay = new HashMap<>();
        mostFailedDay.put(1, 1L);
        mostFailedDay.put(2, 2L);
        mostFailedDay.put(3, 3L);
        mostFailedDay.put(4, 4L);
        mostFailedDay.put(5, 5L);

        given(historyRepository.getMostFaildedDay(any()))
                .willReturn(mostFailedDay);

        //when
        statisticalProcessingScheduler.saveMostFailedDay(monthly);

        //then
        verify(historyRepository).getMostFaildedDay(any());
        verify(statisticsRepository).save(any());
    }
}