package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.statistics.dto.StatisticsCategoryVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsSuccessCategoryAvgVo;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import com.sollertia.habit.global.scheduler.SchedulerUtils;
import com.sollertia.habit.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static com.sollertia.habit.global.scheduler.SchedulerUtils.getSearchDateDto;

@Service
@Transactional
@RequiredArgsConstructor
public class StatisticalProcessingScheduler {

    private final HistoryRepository historyRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final HabitRepository habitRepository;
    private final StatisticsRepository statisticsRepository;
    private final MonsterRepository monsterRepository;
    private final RedisUtil redisUtil;

    public void statisticsMonthMaxMinusByCategory(SessionType durationEnum) {

        SearchDateDto duration = getSearchDateDto(durationEnum);

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

        Statistics statistics = new Statistics(contents, category.toString(), SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void statisticsAvgAchievementPercentageByCategory(SessionType durationEnum) {

        Category[] categories = Category.values();
        for (Category c : categories) {
            redisUtil.deleteData(c.toString());
        }

        SearchDateDto duration = SchedulerUtils.getSearchDateDto(durationEnum);

        List<StatisticsSuccessCategoryAvgVo> list = completedHabitRepository.statisticsAvgAchievementPercentageByCategory(duration);

        List<Statistics> statisticsList = new ArrayList<>();
        for (StatisticsSuccessCategoryAvgVo vo : list) {
            int avg = (int) Math.round(vo.getAvgPer());
            redisUtil.setDataExpire(vo.getCategory().toString(), Integer.toString(avg));
            String contents = "지난 달" + vo.getCategory().toString() + "의 평균 성공률";
            String value = avg + "%";
            statisticsList.add(new Statistics(contents, value,  SessionType.MONTHLY));
        }

        statisticsRepository.saveAll(statisticsList);
    }

    public void statisticsMaxSelectedByCategory(SessionType durationEnum) {

        SearchDateDto duration = SchedulerUtils.getSearchDateDto(durationEnum);
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

        Statistics statistics = new Statistics(contents, resultCategory.toString(), SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void maintainNumberOfHabitByUser() {
        int habits = (int) habitRepository.count();
        List<Long> users = habitRepository.statisticsGetNumberOfUser();
        int totalUser = users.size();

        String contents = "현재 사용자들이 평균적으로 유지하고 있는 습관의 총 개수";
        String value = Integer.toString(Math.round(habits / totalUser));

        Statistics statistics = new Statistics(contents, value, SessionType.MONTHLY);
        statisticsRepository.save(statistics);
    }

    public void saveMonsterTypeStatistics(SessionType sessionType) {
        SearchDateDto duration = getSearchDateDto(sessionType);
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

        String contentsMax = sessionType.getString()
                + "가장 많이 선택된 몬스터는 누구일까요?";

        String contentMin = sessionType.getString()
                + "가장 적게 선택된 몬스터는 누구일까요?";

        String valueMax = sessionType.getString()
                + "가장 많이 선택된 monster는 "
                + max.get().getKey() + "이며"
                + "총" + max.get().getValue()
                + "회 선택받았습니다!";

        String valueMin = sessionType.getString()
                + "가장 적게 선택된 monster는 "
                + min.get().getKey() + "이며"
                + "총" + min.get().getValue()
                + "회 선택받았습니다!";

        statisticsRepository.save(new Statistics(contentsMax, valueMax, sessionType));
        statisticsRepository.save(new Statistics(contentMin, valueMin, sessionType));

    }

    public void saveMostFailedDay(SessionType sessionType) {
        SearchDateDto searchDateDto = getSearchDateDto(sessionType);
        Map<String, Integer> mostFaildedDay = historyRepository.getMostFaildedDay(searchDateDto);

        Optional<Map.Entry<String, Integer>> max = mostFaildedDay
                .entrySet()
                .stream()
                .max((Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2)
                        -> e1.getValue().compareTo(e2.getValue()));

        String valueMax = sessionType.getString()
                + "사람들은 어떤 요일에 가장 많이 습관을 실패했을까요?";


        String content = sessionType.getString()
                + "가장 많은 유저가"
                + max.get().getKey()
                + "요일에 습관 달성을 실패했습니다."
                + "총" + max.get().getValue()
                + "번의 습관 실패가 기록되어 있네요";

        statisticsRepository.save(new Statistics(valueMax, content, sessionType));
    }

}
