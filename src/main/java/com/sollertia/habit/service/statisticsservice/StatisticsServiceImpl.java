package com.sollertia.habit.service.statisticsservice;

import com.sollertia.habit.domain.completedhabbit.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.CompletedHabitRepository;
import com.sollertia.habit.domain.completedhabbit.SimpleHabitVo;
import com.sollertia.habit.domain.completedhabbit.StatisticsResponseDto;
import com.sollertia.habit.domain.user.User;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService{

    private final CompletedHabitRepository completedHabitRepository;

    @Override
    public StatisticsResponseDto getStatistics(User user) {
        LocalDate now = LocalDate.now();

        List<SimpleHabitVo> successVoList = new ArrayList<>();
        List<SimpleHabitVo> failedVoList = new ArrayList<>();

        List<CompletedHabit> successHabit = completedHabitRepository.findAllByUserAndIsSuccessTrueAndCreatedAtBetween(user,
                now.with(TemporalAdjusters.firstDayOfMonth()),
                now.with(TemporalAdjusters.lastDayOfMonth()));

        for (CompletedHabit completedHabit : successHabit) {
            successVoList.add(new SimpleHabitVo(completedHabit));
        }

        List<CompletedHabit> failedHabit = completedHabitRepository.findAllByUserAndIsSuccessFalseAndCreatedAtBetween(user,
                now.with(TemporalAdjusters.firstDayOfMonth()),
                now.with(TemporalAdjusters.lastDayOfMonth()));

        for (CompletedHabit completedHabit : failedHabit) {
            failedVoList.add(new SimpleHabitVo(completedHabit));
        }

        StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto(successHabit, failedHabit);
        statisticsResponseDto.setStatusCode(200);
        statisticsResponseDto.setMsg("success");

        return statisticsResponseDto;

    }

}
