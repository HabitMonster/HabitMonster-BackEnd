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
    private final HabitRepository habitRepository;
    private final MonsterRepository monsterRepository;
    private final HistoryRepository historyRepository;
    private final StatisticsRepository statisticsRepository;
    private final UserRepository userRepository;
    private final RecommendationRepository recommendationRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void runWhenEveryMidNight() {
        LocalDate date = LocalDate.now();
        minusExpOnLapsedHabit(date);
        expireHabit(date);
    }

    @Scheduled(cron = "0 0 1 ? * SUN")
    @Transactional
    public void runWhenEveryWeek() {
        dataManagingScheduler.makePreset();
        makeRecommendations();
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

    private void minusExpOnLapsedHabit(LocalDate date) {
        String day = String.valueOf(date.minusDays(1).getDayOfWeek().getValue());
        List<Habit> habitsWithDaysAndAccomplish = habitRepository.findHabitsWithDaysAndAccomplish(day, false);

        for (Habit habit : habitsWithDaysAndAccomplish) {
            minusExpFromOwnerOf(habit);
            makeHistoryOf(habit);
        }
        habitRepository.updateAccomplishInSessionToFalse();
    }

    private void minusExpFromOwnerOf(Habit habit) {
        Monster monster = monsterRepository.findByUserId(habit.getUser().getId()).orElseThrow(
                () -> new MonsterNotFoundException(habit.getUser().getSocialId() + "의 몬스터를 찾을 수 없습니다."));
        monster.minusExpPoint();
        monsterRepository.saveAndFlush(monster);
    }

    private void makeHistoryOf(Habit habit) {
        History history = History.makeHistory(habit);
        historyRepository.save(history);
    }

    private void expireHabit(LocalDate date) {
        List<Habit> habitListForDelete = habitRepository.findAllByDurationEndLessThan(date);
        log.info("Habit count for delete: " + habitListForDelete.size());

        moveToCompletedHabitList(habitListForDelete);
        deleteHabitList(habitListForDelete);
    }

    private void moveToCompletedHabitList(List<Habit> habitListForDelete) {
        List<CompletedHabit> completedHabitList = CompletedHabit.listOf(habitListForDelete);
        completedHabitRepository.saveAll(completedHabitList);
    }

    private void deleteHabitList(List<Habit> habitListForDelete) {
        List<Long> habitIdListForDelete = habitListForDelete.stream().map(Habit::getId).collect(Collectors.toList());
        habitRepository.deleteAllById(habitIdListForDelete);
    }



    public void saveMonsterTypeStatistics(SessionType durationEnum) {
        SearchDateDto duration = SchedulerUtils.getSearchDateDto(durationEnum);
        monsterRepository.getMonsterTypeCount(duration);
        // 이후 최대값 최소값 찾아 String 만들고 statisticsRepository 저장

    }

    private void makeRecommendations() {
        log.info("Start Remake Recommendations");
        RecommendationType[] values = RecommendationType.values();
        List<Recommendation> recommendationList = new ArrayList<>();

        for (RecommendationType value : values) {
            List<User> top10List = getTop10(value);
            recommendationList.addAll(Recommendation.listOf(top10List, value));
        }
        recommendationRepository.deleteAll();
        recommendationRepository.saveAll(recommendationList);
        log.info("End Remake Recommendations");
    }

    private List<User> getTop10(RecommendationType value) {
        if (value.getId() <= 8) {
            return userRepository.searchTop10ByCategory(Category.getCategory(value.getId()));
        } else if (value.getId() == 9) {
            return userRepository.searchTop10ByFollow();
        } else {
            return new ArrayList<>();
        }
    }




}

