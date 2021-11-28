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

    public void saveMonsterTypeStatistics(SessionType durationEnum) {
        SearchDateDto duration = SchedulerUtils.getSearchDateDto(durationEnum);
        monsterRepository.getMonsterTypeCount(duration);
        // 이후 최대값 최소값 찾아 String 만들고 statisticsRepository 저장

    }

    public void statisticsMonthMaxMinusByCategory(DurationEnum durationEnum) {

        SearchDateDto duration = getSearchDateDto(durationEnum);
        LocalDate lastMonth = LocalDate.now().minusMonths(1L);

        List<StatisticsCategoryVo> list = historyRepository.statisticsMonthMaxMinusByCategory(duration);
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

    public void statisticsAvgAchievementPercentageByCategory(DurationEnum durationEnum) {

        Category[] categories = Category.values();
        for (Category c : categories) {
            redisUtil.deleteData(c.toString());
        }

        SearchDateDto duration = getSearchDateDto(durationEnum);
        LocalDate lastMonth = LocalDate.now().minusMonths(1L);

        List<StatisticsSuccessCategoryAvgVo> list = completedHabitRepository.statisticsAvgAchievementPercentageByCategory(duration);

        List<Statistics> statisticsList = new ArrayList<>();
        for (StatisticsSuccessCategoryAvgVo vo : list) {
            int avg = (int) Math.round(vo.getAvgPer());
            redisUtil.setDataExpire(vo.getCategory().toString(), Integer.toString(avg));
            String result = lastMonth.getMonth().getValue() + "월 한달동안 " + vo.getCategory().toString() +
                    "의 평균 성공률은 " + avg + "%입니다.";
//            statisticsList.add(new Statistics(result, SessionType.MONTHLY));
        }

        statisticsRepository.saveAll(statisticsList);
    }

    public void statisticsMaxSelectedByCategory(DurationEnum durationEnum) {

        SearchDateDto duration = getSearchDateDto(durationEnum);
        LocalDate lastMonth = LocalDate.now().minusMonths(1L);
        LocalDate start = lastMonth.withDayOfMonth(1);
        LocalDate end = lastMonth.withDayOfMonth(lastMonth.lengthOfMonth());

        List<StatisticsCategoryVo> completedList = completedHabitRepository.statisticsMaxSelectedByCategory(start, end);
        List<StatisticsCategoryVo> habitList = habitRepository.statisticsMaxSelectedByCategory(duration);

        Category resultCategory = Category.Hobby;
        Long num = 0L;

        Map<Category, Long> sumList = new HashMap<>();

        for (StatisticsCategoryVo vo : completedList) {
            sumList.put(vo.getCategory(), vo.getNum());
        }

        for (StatisticsCategoryVo vo : habitList) {
            if (sumList.containsKey(vo.getCategory())) {
                sumList.put(vo.getCategory(), sumList.get(vo.getCategory()) + vo.getNum());
            } else {
                sumList.put(vo.getCategory(), vo.getNum());
            }
        }

        Set<Category> keySet = sumList.keySet();

        for (Category tempKey : keySet) {
            if (num <= sumList.get(tempKey)) {
                num = sumList.get(tempKey);
                resultCategory = tempKey;
            }
        }

        String result = lastMonth.getMonth().getValue() + "월 한달동안 가장 많은 사람이 선택한 카테고리는 " + resultCategory.toString() + "입니다.";

//        Statistics statistics = new Statistics(result, SessionType.MONTHLY);
//        statisticsRepository.save(statistics);
    }

    public void maintainNumberOfHabitByUser() {
        int habits = (int) habitRepository.count();
        List<Long> users = habitRepository.statisticsGetNumberOfUser();
        int totalUser = users.size();

        String result = "현재 사용자들이 평균적으로 유지하고 있는 습관의 개수는 " + Math.round(habits / totalUser) + "개입니다.";

//        Statistics statistics = new Statistics(result, SessionType.MONTHLY);
//        statisticsRepository.save(statistics);
    }


}

