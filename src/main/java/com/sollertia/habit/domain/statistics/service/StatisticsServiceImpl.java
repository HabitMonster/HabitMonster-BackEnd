package com.sollertia.habit.domain.statistics.service;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitVo;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.user.entity.User;
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

}
