package com.sollertia.habit.domain.habit.service;


import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.entity.HabitWithTimer;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithTimerRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.habit.HabitIdNotFoundException;
import com.sollertia.habit.global.exception.user.HasNoPermissionException;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;

    private final HabitWithCounterRepository habitWithCounterRepository;

    private final HabitWithTimerRepository habitWithTimerRepository;

    private final HistoryRepository historyRepository;

    private final CompletedHabitRepository completedHabitRepository;

    private final MonsterService monsterService;

    @Transactional
    @Override
    public HabitDetailResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto, User user) {

        Habit habit = Habit.createHabit(habitTypeDto.getHabitType(), createHabitRequestDto, user);
        Habit savedHabit = (Habit) habitRepository.save(habit);

        HabitDetail habitDetail = HabitDetail.builder()
                .habitId(savedHabit.getId())
                .category(savedHabit.getCategory())
                .count(savedHabit.getGoalInSession())
                .totalCount(savedHabit.getTotalCount())
                .description(savedHabit.getDescription())
                .durationEnd(savedHabit.getDurationEnd().toString())
                .durationStart(savedHabit.getDurationStart().toString())
                .achievePercentage(savedHabit.getAchievePercentage())
                .practiceDays(savedHabit.getPracticeDays())
                .current(savedHabit.getCurrent())
                .categoryId(savedHabit.getCategory().getCategoryId())
                .title(savedHabit.getTitle())
                .isAccomplished(false)
                .build();

        return HabitDetailResponseDto.builder()
                .habit(habitDetail)
                .statusCode(200)
                .responseMessage("Habit registered Completed")
                .build();

    }

    @Override
    public HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) {


        Habit foundHabit = getHabitFromRepository(habitTypeDto, habitId);

        HabitDetail build = HabitDetail.builder()
                .habitId(foundHabit.getId())
                .category(foundHabit.getCategory())
                .categoryId(foundHabit.getCategory().getCategoryId())
                .count(foundHabit.getGoalInSession())
                .totalCount(foundHabit.getTotalCount())
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
    @Transactional
    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(
                () -> new HabitIdNotFoundException("Not Found habit"));

        if ( isOwner(user, habitWithCounter) ) {
            user.getHabit().remove(habitWithCounter);
            monsterService.minusExpWithCount(user, habitWithCounter.getAccomplishCounter());
            habitRepository.delete(habitWithCounter);
        } else {
            throw new HasNoPermissionException("User has no permission");
        }

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
    @Transactional
    public HabitDetailResponseDto updateHabit(Long habitId, HabitUpdateRequestDto habitUpdateRequestDto, User user) {

        HabitWithCounter habit = habitWithCounterRepository.findById(habitId)
                .orElseThrow(() -> new HabitIdNotFoundException("Not Found Habit"));

        if ( isOwner(user, habit) ) {
            habit.updateHabit(habitUpdateRequestDto);
        } else {
            throw new HasNoPermissionException("User has no permission");
        }

        HabitDetail build = HabitDetail.of(habit);
        return HabitDetailResponseDto.builder()
                .habit(build)
                .statusCode(200)
                .responseMessage("Habit updated")
                .build();

    }

    public HabitSummaryListResponseDto getHabitSummaryListResponseDto(User user) {
        return HabitSummaryListResponseDto.builder()
                .habits(getHabitSummaryList(user))
                .totalHabitCount(getAllHabitCountByUser(user))
                .responseMessage("Habit Detail List Query Completed")
                .statusCode(200)
                .build();
    }

    private Habit getHabitFromRepository(HabitTypeDto habitTypeDto, Long habitId) {
        Habit foundHabit = null;

        switch (habitTypeDto.getHabitType()) {
            case HABITWITHCOUNTER:
                foundHabit = habitWithCounterRepository.findById(habitId).orElseThrow(
                        () -> new HabitIdNotFoundException("Not Found Habit"));
                break;
            case HABITWITHTIMER:
                foundHabit = habitWithTimerRepository.findById(habitId).orElseThrow(
                        () -> new HabitIdNotFoundException("Not Found Habit"));

                break;
        }
        return foundHabit;
    }

    public List<HabitSummaryVo> getHabitListByUser(User user) {
        List<Habit> habits = habitRepository.findByUserOrderByCreatedAtDesc(user);
        return HabitSummaryVo.listOf(habits);
    }

    public Integer getAllHabitCountByUser(User user) {
        Integer currentHabitCount = habitRepository.countByUser(user);
        Integer complatedHabitCount = completedHabitRepository.countByUser(user);
        return currentHabitCount+complatedHabitCount;
    }

    private boolean isOwner(User user, HabitWithCounter habitWithCounter) {
        return habitWithCounter.getUser().getId().equals(user.getId());
    }
}
