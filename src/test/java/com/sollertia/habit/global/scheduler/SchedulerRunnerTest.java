package com.sollertia.habit.global.scheduler;

import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.scheduler.service.DataManagingScheduler;
import com.sollertia.habit.global.scheduler.service.StatisticalProcessingScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class SchedulerRunnerTest {

    @InjectMocks
    private SchedulerRunner schedulerRunner;

    @Mock
    private DataManagingScheduler dataManagingScheduler;
    @Mock
    private StatisticalProcessingScheduler statisticalProcessingScheduler;
    @Mock
    private StatisticsRepository statisticsRepository;

    @Test
    void runWhenEveryMidNight() {
        schedulerRunner.runWhenEveryMidNight();
        LocalDate now = LocalDate.now();
        verify(dataManagingScheduler).dropUserIfDisabled();
        verify(dataManagingScheduler).minusExpOnLapsedHabit(now);
        verify(dataManagingScheduler).expireHabit(now);
    }

    @Test
    void runWhenEveryWeek() {
        schedulerRunner.runWhenEveryWeek();
        verify(dataManagingScheduler).makeRecommendations();
        verify(dataManagingScheduler).makePreset();
        verify(statisticsRepository).deleteAllBySessionType(SessionType.WEEKLY);
        verify(statisticalProcessingScheduler).saveMonsterTypeStatistics(SessionType.WEEKLY);
    }

    @Test
    void runWhenEveryMonth() {
        schedulerRunner.runWhenEveryMonth();
        verify(statisticsRepository).deleteAllBySessionType(SessionType.MONTHLY);
        verify(statisticalProcessingScheduler).statisticsMonthMaxMinusByCategory(SessionType.MONTHLY);
        verify(statisticalProcessingScheduler).statisticsAvgAchievementPercentageByCategory(SessionType.MONTHLY);
        verify(statisticalProcessingScheduler).statisticsMaxSelectedByCategory(SessionType.MONTHLY);
        verify(statisticalProcessingScheduler).maintainNumberOfHabitByUser();
        verify(statisticalProcessingScheduler).saveMostFailedDay(SessionType.MONTHLY);
        verify(statisticalProcessingScheduler).saveMonsterTypeStatistics(SessionType.MONTHLY);
    }
}