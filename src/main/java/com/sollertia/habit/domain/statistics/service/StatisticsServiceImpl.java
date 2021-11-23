package com.sollertia.habit.domain.statistics.service;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitVo;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsResponseDto;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsVo;
import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final CompletedHabitRepository completedHabitRepository;
    private final StatisticsRepository statisticsRepository;

    @Override
    public StatisticsResponseDto getStatistics(User user, String date) {
        String datenow = date + "-01";
        LocalDate now = LocalDate.parse(datenow, DateTimeFormatter.ISO_DATE);

        List<SimpleHabitVo> simpleHabitVoList = new ArrayList<>();

        List<CompletedHabit> habitList = completedHabitRepository.findAllByUserAndStartDateBetweenOrderByStartDate(user,
                now.with(TemporalAdjusters.firstDayOfMonth()),
                now.with(TemporalAdjusters.lastDayOfMonth()));
        int succeededCount = (int) habitList.stream().filter(completedHabit -> completedHabit.getIsSuccess()).count();
        int failedCount = (int) habitList.stream().filter(completedHabit -> !completedHabit.getIsSuccess()).count();

        for (CompletedHabit completedHabit : habitList) {
            simpleHabitVoList.add(new SimpleHabitVo(completedHabit));
        }

        return StatisticsResponseDto.builder()
                .habitList(simpleHabitVoList)
                .succeededCount(succeededCount)
                .failedCount(failedCount)
                .totalCount(habitList.size())
                .responseMessage("Statistics Query Completed")
                .statusCode(200)
                .build();
    }

    @Override
    public GlobalStatisticsResponseDto getGlobalStatistics() {
        List<Statistics> statisticsList = statisticsRepository.findAll();
        int length = statisticsList.size();
        if (length == 0) {
            return emptyGlobalStatisticsDto();
        }

        int[] randomNumbers = getRandomNumbers(length);
        List<GlobalStatisticsVo> statisticsVoList = extractRandomStatistics(statisticsList, randomNumbers);
        return GlobalStatisticsResponseDto.builder()
                .statistics(statisticsVoList)
                .statusCode(200)
                .responseMessage("Global Statistics Query Completed")
                .build();
    }

    private GlobalStatisticsResponseDto emptyGlobalStatisticsDto() {
        return GlobalStatisticsResponseDto.builder()
                .responseMessage("Global Statistics is Empty")
                .statusCode(200)
                .build();
    }

    private List<GlobalStatisticsVo> extractRandomStatistics(List<Statistics> statisticsList, int[] randomNumbers) {
        List<GlobalStatisticsVo> globalStatisticsVoList = new ArrayList<>();
        for (int number : randomNumbers) {
            Statistics statistics = statisticsList.get(number);
            globalStatisticsVoList.add(GlobalStatisticsVo.of(statistics));
        }
        return globalStatisticsVoList;
    }

    private int[] getRandomNumbers(int max) {
        int size = 5;
        if ( max < size ) {
            size = max;
        }
        Random random = new Random();
        return random.ints(0, max)
                .distinct()
                .limit(size)
                .toArray();
    }
}
