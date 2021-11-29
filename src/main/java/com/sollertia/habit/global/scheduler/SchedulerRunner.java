package com.sollertia.habit.global.scheduler;


import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.scheduler.service.DataManagingScheduler;
import com.sollertia.habit.global.scheduler.service.StatisticalProcessingScheduler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@Slf4j(topic = "SCHEDULER_FILE_LOGGER")
@Profile("scheduler")
@RequiredArgsConstructor
public class SchedulerRunner {

    private final DataManagingScheduler dataManagingScheduler;
    private final StatisticalProcessingScheduler statisticalProcessingScheduler;
    private final StatisticsRepository statisticsRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void runWhenEveryMidNight() {
        LocalDate date = LocalDate.now();
        dataManagingScheduler.dropUserIfDisabled();
        dataManagingScheduler.minusExpOnLapsedHabit(date);
        dataManagingScheduler.expireHabit(date);
    }

    @Scheduled(cron = "0 0 1 ? * MON")
    @Transactional
    public void runWhenEveryWeek() {
        dataManagingScheduler.makeRecommendations();
        dataManagingScheduler.makePreset();
        statisticsRepository.deleteAllBySessionType(SessionType.WEEKLY);
        statisticalProcessingScheduler.saveMonsterTypeStatistics(SessionType.WEEKLY);
    }

    @Scheduled(cron = "0 30 1 1 * *")
    @Transactional
    public void runWhenEveryMonth() {
        statisticsRepository.deleteAllBySessionType(SessionType.MONTHLY);
        statisticalProcessingScheduler.statisticsMonthMaxMinusByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.statisticsAvgAchievementPercentageByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.statisticsMaxSelectedByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.maintainNumberOfHabitByUser();
        statisticalProcessingScheduler.saveMostFailedDay(SessionType.MONTHLY);
        statisticalProcessingScheduler.saveMonsterTypeStatistics(SessionType.MONTHLY);
    }
}

