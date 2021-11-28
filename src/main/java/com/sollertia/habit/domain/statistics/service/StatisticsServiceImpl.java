package com.sollertia.habit.domain.statistics.service;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitDto;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsDto;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsResponseDto;
import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.RandomUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final CompletedHabitRepository completedHabitRepository;
    private final StatisticsRepository statisticsRepository;
    private final RandomUtil randomUtil;

    @Override
    public StatisticsResponseDto getStatistics(User user, String date) {
        String datenow = date + "-01";
        LocalDate now = LocalDate.parse(datenow, DateTimeFormatter.ISO_DATE);

        List<SimpleHabitDto> simpleHabitDtoList = new ArrayList<>();

        List<CompletedHabit> habitList = completedHabitRepository.findAllByUserAndStartDateBetweenOrderByStartDate(user,
                now.with(TemporalAdjusters.firstDayOfMonth()),
                now.with(TemporalAdjusters.lastDayOfMonth()));
        int succeededCount = (int) habitList.stream().filter(completedHabit -> completedHabit.getIsSuccess()).count();
        int failedCount = (int) habitList.stream().filter(completedHabit -> !completedHabit.getIsSuccess()).count();

        for (CompletedHabit completedHabit : habitList) {
            simpleHabitDtoList.add(new SimpleHabitDto(completedHabit));
        }

        return StatisticsResponseDto.builder()
                .habitList(simpleHabitDtoList)
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

        int[] randomNumbers = randomUtil.getRandomNumbers(length);
        List<GlobalStatisticsDto> statisticsVoList = extractRandomStatistics(statisticsList, randomNumbers);
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

    private List<GlobalStatisticsDto> extractRandomStatistics(List<Statistics> statisticsList, int[] randomNumbers) {
        List<GlobalStatisticsDto> globalStatisticsDtoList = new ArrayList<>();
        for (int number : randomNumbers) {
            Statistics statistics = statisticsList.get(number);
            globalStatisticsDtoList.add(GlobalStatisticsDto.of(statistics));
        }
        return globalStatisticsDtoList;
    }
}
