package com.sollertia.habit.global.utils;


import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.service.PreSetServiceImpl;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
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
import com.sollertia.habit.global.globalenum.DurationEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j(topic = "SCHEDULER_FILE_LOGGER")
@RequiredArgsConstructor
public class SchedulerRunner {

    private final PreSetRepository preSetRepository;
    private final HabitWithCounterRepository habitWithCounterRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final PreSetServiceImpl preSetService;
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
        makeRecommendations();
    }

//    @Scheduled(cron = "0 0 1 ? * SUN")
//    public void runWhenEveryMonth() {
//        makePreset();
//    }
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


    public void saveMonsterTypeStatistics(DurationEnum durationEnum) {
        SearchDateDto duration = getSearchDateDto(durationEnum);
        Map<String, Integer> monsterTypeCount = monsterRepository.getMonsterTypeCount(duration);

        Optional<Map.Entry<String, Integer>> max = monsterTypeCount
                .entrySet()
                .stream()
                .max((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2)
                        -> e1.getValue().compareTo(e2.getValue()));

        Optional<Map.Entry<String, Integer>> min = monsterTypeCount
                .entrySet()
                .stream()
                .min((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2)
                        -> e1.getValue().compareTo(e2.getValue()));

        String contentsMax = "지난 "
                + "가장 많이 선택된 monster는 "
                + max.get().getKey() + "이며"
                + "총" + max.get().getValue()
                + "회 선택받았습니다!";

        String contentsMin = "지난 "
                + "가장 적게 선택된 monster는 "
                + min.get().getKey() + "이며"
                + "총" + min.get().getValue()
                + "회 선택받았습니다!";
//        //세션 타입 일치 필요 일단은 그냥 monthly로 해둠
//        statisticsRepository.save(new Statistics(contentsMax, SessionType.MONTHLY));
//        statisticsRepository.save(new Statistics(contentsMin, SessionType.MONTHLY));

    }

    public void saveMostFailedDay(DurationEnum durationEnum) {
        SearchDateDto searchDateDto = getSearchDateDto(durationEnum);
        Map<String, Integer> mostFaildedDay = historyRepository.getMostFaildedDay(searchDateDto);

        Optional<Map.Entry<String, Integer>> max = mostFaildedDay
                .entrySet()
                .stream()
                .max((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2)
                        -> e1.getValue().compareTo(e2.getValue()));

        String content = "지난 "
                + "가장 많은 유저가"
                + max.get().getKey()
                + "요일에 습관 달성을 실패했습니다."
                + "총" + max.get().getValue()
                + "번의 습관 실패가 기록되어 있네요";
//
//        statisticsRepository.save(new Statistics(content, SessionType.MONTHLY));
    }

    public SearchDateDto getSearchDateDto(DurationEnum durationEnum) {
        LocalDate schedulerNow = LocalDate.now();
        SearchDateDto result = null;
        switch (durationEnum) {
            case DAILY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusDays(1),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

            case WEEKLY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusDays(8),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

            case MONTHLY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusMonths(1),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

        }
        return result;
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
        if ( value.getId() <= 8 ) {
            return userRepository.searchTop10ByCategory(Category.getCategory(value.getId()));
        } else if ( value.getId() == 9 ) {
            return userRepository.searchTop10ByFollow();
        } else {
            return new ArrayList<>();
        }
    }
    public void statisticsMonthMaxMinusByCategory() {

        LocalDate lastMonth = LocalDate.now().minusMonths(1L);
        LocalDate start = lastMonth.withDayOfMonth(1);
        LocalDate end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

        List<StatisticsCategoryVo> list = historyRepository.statisticsMonthMaxMinusByCategory(start.atStartOfDay(), end.atStartOfDay());
        Category category = Category.Health;
        Long num = 0L;
        for (StatisticsCategoryVo vo : list) {
            if (num <= vo.getNum()) {
                num = vo.getNum();
                category = vo.getCategory();
            }
        }
        String result = lastMonth.getMonth().getValue() + "월 한달동안 가장 많은 감점을 받은 카테고리는 " + category.toString() + "입니다.";

//        Statistics statistics = new Statistics(result, SessionType.MONTHLY);
//        statisticsRepository.save(statistics);
    }

    public void statisticsAvgAchievementPercentageByCategory() {

        LocalDate lastMonth = LocalDate.now().minusMonths(1L);
        LocalDate start = lastMonth.withDayOfMonth(1);
        LocalDate end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

//        List<StatisticsSuccessCategoryAvgVo> list = completedHabitRepository.statisticsAvgAchievementPercentageByCategory(start.atStartOfDay(), end.atStartOfDay());

        List<Statistics> statisticsList = new ArrayList<>();
//        for (StatisticsSuccessCategoryAvgVo vo : list) {
//            int avg = (int) Math.round(vo.getAvgPer());
//            String result = lastMonth.getMonth().getValue() + "월 한달동안 " + vo.getCategory().toString() +
//                    "의 평균 성공률은 " + avg + "%입니다.";
//            statisticsList.add(new Statistics(result, SessionType.MONTHLY));
//        }

        statisticsRepository.saveAll(statisticsList);
    }

    public void statisticsMaxSelectedByCategory(){


        LocalDate lastMonth = LocalDate.now().minusMonths(1L);
        LocalDate start = lastMonth.withDayOfMonth(1);
        LocalDate end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

        List<StatisticsCategoryVo> completedList = completedHabitRepository.statisticsMaxSelectedByCategory(start,end);

        Category category = Category.Health;
        Long num = 0L;
        for (StatisticsCategoryVo vo : completedList) {
            if (num <= vo.getNum()) {
                num = vo.getNum();
                category = vo.getCategory();
            }
        }

        String result = lastMonth.getMonth().getValue() + "월 한달동안 가장 많은 사람이 선택한 카테고리는 " + category.toString() + "입니다.";

//        Statistics statistics = new Statistics(result, SessionType.MONTHLY);
//        statisticsRepository.save(statistics);
    }


}

