package com.sollertia.habit.domain.habit.service;


import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.habit.HabitIdNotFoundException;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;

    private final HabitWithCounterRepository habitWithCounterRepository;

    private final HistoryRepository historyRepository;

    private final CompletedHabitRepository completedHabitRepository;

    private final MonsterService monsterService;

    @Transactional
    @Override
    public HabitDetailResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto, User user) {

        //임시 수정, getGoalCount override해서 형변환 없이 리팩토링 하기
        HabitWithCounter habit = (HabitWithCounter) Habit.createHabit(habitTypeDto.getHabitType(), createHabitRequestDto, user);
        HabitWithCounter savedHabit = (HabitWithCounter) habitRepository.save(habit);

        HabitDetail build = HabitDetail.builder()
                .habitId(savedHabit.getId())
                .category(savedHabit.getCategory().toString())
                .count(savedHabit.getGoalCountInSession())
                .totalCount(Math.toIntExact(savedHabit.getGoalCountInSession() * savedHabit.getWholeDays()))
                .description(savedHabit.getDescription())
                .durationEnd(savedHabit.getDurationEnd().toString())
                .durationStart(savedHabit.getDurationStart().toString())
                .achievePercentage(savedHabit.getAchievePercentage())
                .practiceDays(savedHabit.getPracticeDays())
                .current(savedHabit.getCurrent())
                .title(savedHabit.getTitle())
                .build();

        return HabitDetailResponseDto.builder()
                .habit(build)
                .statusCode(200)
                .responseMessage("Habit registered Completed")
                .build();

    }

    @Override
    public HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) {

        HabitWithCounter foundHabit = habitWithCounterRepository.findById(habitId).orElseThrow(
                () -> new HabitIdNotFoundException("Not Found Habit"));

        HabitDetail build = HabitDetail.builder()
                .habitId(foundHabit.getId())
                .category(foundHabit.getCategory().toString())
                .count(foundHabit.getGoalCountInSession())
                .totalCount(Math.toIntExact(foundHabit.getGoalCountInSession() * foundHabit.getWholeDays()))
                .description(foundHabit.getDescription())
                .durationEnd(foundHabit.getDurationEnd().toString())
                .durationStart(foundHabit.getDurationStart().toString())
                .achievePercentage(foundHabit.getAchievePercentage())
                .practiceDays(foundHabit.getPracticeDays())
                .current(foundHabit.getCurrent())
                .title(foundHabit.getTitle())
                .build();

        return HabitDetailResponseDto.builder()
                .habit(build)
                .responseMessage("Habit Detail Query Completed")
                .statusCode(200)
                .build();
    }

    @Override
    public HabitCheckResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(
                () -> new HabitIdNotFoundException("Not Found Habit"));
        Boolean isAchieve = habitWithCounter.check(1L);
        HabitWithCounter checkedHabit = habitWithCounterRepository.save(habitWithCounter);
        HabitSummaryVo habitSummaryVo = HabitSummaryVo.of(checkedHabit);

        if (isAchieve) {
            plusExpPointAndMakeHistory(habitWithCounter);
            deleteHabitIfCompleteToday(habitWithCounter);
        }

        return HabitCheckResponseDto.builder()
                .statusCode(200)
                .responseMessage("Check Habit Completed")
                .habit(habitSummaryVo)
                .build();

    }

    private void plusExpPointAndMakeHistory(HabitWithCounter habitWithCounter) {
        monsterService.plusExpPoint(habitWithCounter.getUser());
        History history = History.makeHistory(habitWithCounter);
        historyRepository.save(history);
    }

    private void deleteHabitIfCompleteToday(HabitWithCounter habitWithCounter) {
        if (habitWithCounter.getDurationEnd().equals(LocalDate.now())) {
            CompletedHabit completedHabit = CompletedHabit.of(habitWithCounter);
            completedHabitRepository.save(completedHabit);
            habitWithCounterRepository.delete(habitWithCounter);
        }
    }

    @Override
    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(
                () -> new HabitIdNotFoundException("Not Found habit"));

        user.getHabit().remove(habitWithCounter);
        habitRepository.delete(habitWithCounter);

        return DefaultResponseDto.builder()
                .statusCode(200)
                .responseMessage("Habit Delete Completed")
                .build();
    }


    @Override
    public List<HabitSummaryVo> getHabitSummaryList(User user) {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfWeek().getValue();

        List<Habit> habits = habitRepository.findTodayHabitListByUser(user, day, today);
        return HabitSummaryVo.listOf(habits);
    }

    @Override
    public HabitDetailResponseDto updateHabit(Long habitId, HabitUpdateRequestDto habitUpdateRequestDto) {

        HabitWithCounter habit = habitWithCounterRepository.findById(habitId)
                .orElseThrow(() -> new HabitIdNotFoundException("Not Found Habit"));

        habit.updateHabit(habitUpdateRequestDto);

        habitRepository.save(habit);

        HabitDetail build = HabitDetail.builder()
                .habitId(habit.getId())
                .category(habit.getCategory().toString())
                .count(habit.getGoalCountInSession())
                .totalCount(Math.toIntExact(habit.getGoalCountInSession() * habit.getWholeDays()))
                .description(habit.getDescription())
                .durationEnd(habit.getDurationEnd().toString())
                .durationStart(habit.getDurationStart().toString())
                .achievePercentage(habit.getAchievePercentage())
                .current(habit.getCurrent())
                .practiceDays(habit.getPracticeDays())
                .title(habit.getTitle())
                .build();

        return HabitDetailResponseDto.builder()
                .habit(build)
                .statusCode(200)
                .responseMessage("Habit updated")
                .build();

    }

    public HabitSummaryListResponseDto getHabitSummaryListResponseDto(User user) {
        return HabitSummaryListResponseDto.builder()
                .habits(getHabitSummaryList(user))
                .responseMessage("Habit Detail List Query Completed")
                .statusCode(200)
                .build();
    }

    public List<HabitSummaryVo> getHabitListByUser(User user) {
        List<Habit> habits = habitRepository.findByUser(user);
        return HabitSummaryVo.listOf(habits);
    }
}
