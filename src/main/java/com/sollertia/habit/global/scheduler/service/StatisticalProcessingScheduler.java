package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import com.sollertia.habit.global.scheduler.SchedulerUtils;
import com.sollertia.habit.global.scheduler.entity.CategoryAvg;
import com.sollertia.habit.global.scheduler.repository.CategoryAvgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static com.sollertia.habit.global.scheduler.SchedulerUtils.getSearchDateDto;

@Service
@Transactional
@Slf4j(topic = "SCHEDULER_FILE_LOGGER")
@RequiredArgsConstructor
public class StatisticalProcessingScheduler {

    private final HistoryRepository historyRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final HabitRepository habitRepository;
    private final StatisticsRepository statisticsRepository;
    private final MonsterRepository monsterRepository;
    private final CategoryAvgRepository categoryAvgRepository;

    public void statisticsMonthMaxMinusByCategory(SessionType sessionType) {

        SearchDateDto duration = getSearchDateDto(sessionType);

        List<StatisticsCategoryVo> list = historyRepository.statisticsMonthMaxMinusByCategory(duration);
        Category category = Category.Health;
        Long num = 0L;
        for (StatisticsCategoryVo vo : list) {
            if (num <= vo.getNum()) {
                num = vo.getNum();
                category = vo.getCategory();
            }
        }
        String contents = "지난 달 가장 많은 감점을 받은 카테고리는?";

        Statistics statistics = new Statistics(contents, Category.getKorean(category), SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void statisticsAvgAchievementPercentageByCategory(SessionType sessionType) {

        SearchDateDto duration = SchedulerUtils.getSearchDateDto(sessionType);

        List<StatisticsSuccessCategoryAvgVo> list = completedHabitRepository.statisticsAvgAchievementPercentageByCategory(duration);

        List<Statistics> statisticsList = new ArrayList<>();
        for (StatisticsSuccessCategoryAvgVo vo : list) {
            int avg = (int) Math.round(vo.getAvgPer());
            categoryAvgRepository.save(new CategoryAvg(vo.getCategory(), (long) avg));
            String contents = "지난 달 " + Category.getKorean(vo.getCategory()) + "의 평균 성공률";
            String value = avg + "%";
            statisticsList.add(new Statistics(contents, value,  SessionType.MONTHLY));
        }

        statisticsRepository.saveAll(statisticsList);
    }

    public void statisticsMaxSelectedByCategory(SessionType sessionType) {

        SearchDateDto duration = SchedulerUtils.getSearchDateDto(sessionType);
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

        String contents = "지난 달 가장 많은 사람이 선택한 카테고리는?";

        Statistics statistics = new Statistics(contents, Category.getKorean(resultCategory), SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void maintainNumberOfHabitByUser() {
        int habits = (int) habitRepository.count();
        List<Long> users = habitRepository.statisticsGetNumberOfUser();
        int totalUser = users.size();

        String contents = "사용자들이 평균적으로 유지하는 습관 수";
        String value = Integer.toString(Math.round(habits / totalUser));

        Statistics statistics = new Statistics(contents, value, SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void saveMonsterTypeStatistics(SessionType sessionType) {
        log.info("Save monster Type Start");
        SearchDateDto duration = getSearchDateDto(sessionType);
        Map<MonsterType, Long> monsterTypeCount = monsterRepository.getMonsterTypeCount(duration);

        Optional<Map.Entry<MonsterType, Long>> max = monsterTypeCount
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        if (max.isPresent()) {
            SaveMostPickedMonsterStatistics(sessionType, max);
        }

        Optional<Map.Entry<MonsterType, Long>> min = monsterTypeCount
                .entrySet()
                .stream()
                .min(Comparator.comparing(Map.Entry::getValue));

        if (min.isPresent()) {
            SaveMinimumPickedMonster(sessionType, min);
        }

        log.info("Save monster Type End");
    }

    public void saveMostFailedDay(SessionType sessionType) {
        SearchDateDto searchDateDto = getSearchDateDto(sessionType);
        Map<Integer, Long> mostFaildedDay = historyRepository.getMostFaildedDay(searchDateDto);

        Optional<Map.Entry<Integer, Long>> max = mostFaildedDay
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        if (max.isPresent()) {
            String valueMax = sessionType.getString()
                    + " 사람들이 가장 많이 달성을 실패한 요일?";

            String content =
                    SchedulerUtils.daysParseString(max.get().getKey())
                    + ", 총 " + max.get().getValue()
                    + "회";

            statisticsRepository.save(new Statistics(valueMax, content, sessionType));
        }
    }

    private void SaveMinimumPickedMonster(SessionType sessionType, Optional<Map.Entry<MonsterType, Long>> min) {
        String contentMin = sessionType.getString()
                + " 가장 적게 선택된 몬스터는 누구일까요?";

        String valueMin =
                min.get().getKey()
                        + ", 총 " + min.get().getValue()
                        + "회";

        statisticsRepository.save(new Statistics(contentMin, valueMin, sessionType));
    }

    private void SaveMostPickedMonsterStatistics(SessionType sessionType, Optional<Map.Entry<MonsterType, Long>> max) {
        String contentsMax = sessionType.getString()
                + " 가장 많이 선택된 몬스터는 누구일까요?";

        String valueMax =
                max.get().getKey()
                        + ", 총 " + max.get().getValue()
                        + "회";

        statisticsRepository.save(new Statistics(contentsMax, valueMax, sessionType));
    }

}
