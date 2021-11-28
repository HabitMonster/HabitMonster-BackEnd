package com.sollertia.habit.global.scheduler;


import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.service.PreSetServiceImpl;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.domain.user.entity.Recommendation;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import com.sollertia.habit.global.scheduler.service.DataManagingScheduler;
import com.sollertia.habit.global.scheduler.service.StatisticalProcessingScheduler;
import com.sollertia.habit.global.globalenum.DurationEnum;
import com.sollertia.habit.global.scheduler.service.DataManagingScheduler;
import com.sollertia.habit.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j(topic = "SCHEDULER_FILE_LOGGER")
@RequiredArgsConstructor
public class SchedulerRunner {

    private final DataManagingScheduler dataManagingScheduler;
    private final StatisticalProcessingScheduler statisticalProcessingScheduler;

    private final CompletedHabitRepository completedHabitRepository;
    private final PreSetServiceImpl preSetService;
    private final HabitRepository habitRepository;
    private final MonsterRepository monsterRepository;
    private final HistoryRepository historyRepository;
    private final StatisticsRepository statisticsRepository;
    private final RedisUtil redisUtil;
    private final DataManagingScheduler dataManagingScheduler;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void runWhenEveryMidNight() {
        LocalDate date = LocalDate.now();
        dataManagingScheduler.minusExpOnLapsedHabit(date);
        dataManagingScheduler.expireHabit(date);
    }

    @Scheduled(cron = "0 0 1 ? * SUN")
    @Transactional
    public void runWhenEveryWeek() {
        dataManagingScheduler.makeRecommendations();
        dataManagingScheduler.makePreset();
    }

    @Scheduled(cron = "0 0 0 1 * *")
    @Transactional
    public void runWhenEveryMonth() {
        statisticsRepository.deleteAllInBatch();
        statisticalProcessingScheduler.statisticsMonthMaxMinusByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.statisticsAvgAchievementPercentageByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.statisticsMaxSelectedByCategory(SessionType.MONTHLY);
        statisticalProcessingScheduler.maintainNumberOfHabitByUser();
    }

}

