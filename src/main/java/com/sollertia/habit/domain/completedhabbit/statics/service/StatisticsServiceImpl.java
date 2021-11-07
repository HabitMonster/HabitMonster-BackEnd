package com.sollertia.habit.domain.completedhabbit.statics.service;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitVo;
import com.sollertia.habit.domain.completedhabbit.statics.dto.StatisticsResponseDto;
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

        StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto(successVoList, failedVoList);
        statisticsResponseDto.setStatusCode(200);
        statisticsResponseDto.setMsg("success");

        return statisticsResponseDto;

    }

}
