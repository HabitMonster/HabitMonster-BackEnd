package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static com.sollertia.habit.global.scheduler.SchedulerUtils.getSearchDateDto;

@Service
@RequiredArgsConstructor
public class StatisticalProcessingScheduler {

    private final MonsterRepository monsterRepository;
    private final StatisticsRepository statisticsRepository;
    private final HistoryRepository historyRepository;

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
