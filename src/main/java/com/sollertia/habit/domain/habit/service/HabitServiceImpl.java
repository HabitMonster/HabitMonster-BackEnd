package com.sollertia.habit.domain.habit.service;


import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
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

        HabitDetail habitDetail = buildHabitDetail(savedHabit);

        return HabitDetailResponseDto.builder()

                .habit(habitDetail)
                .statusCode(200)
                .responseMessage("Habit registered Completed")
                .build();

    }

    @Override
    public HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) {

        Habit foundHabit = getHabitFromRepository(habitTypeDto, habitId);

        HabitDetail build = buildHabitDetail(foundHabit);

        return HabitDetailResponseDto.builder()
                .habit(build)
                .responseMessage("Habit Detail Query Completed")
                .statusCode(200)
                .build();
    }

    @Override
    public HabitCheckResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId, LocalDate today) {

        Habit foundHabit = getHabitFromRepository(habitTypeDto, habitId);
        Boolean isAchieve = foundHabit.check(1L);
        Habit checkedHabit = (Habit) habitRepository.save(foundHabit);
        HabitSummaryVo habitSummaryVo = HabitSummaryVo.of(foundHabit);

        if (isAchieve) {
            plusExpPointAndMakeHistory(foundHabit);
            deleteHabitIfCompleteToday(foundHabit, today);
        }

        return HabitCheckResponseDto.builder()
                .statusCode(200)
                .responseMessage("Check Habit Completed")
                .habit(habitSummaryVo)
                .build();

    }

    @Override
    @Transactional
    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user) {

        Habit foundHabit = getHabitFromRepository(habitTypeDto, habitId);

        isOwner(user, foundHabit);

        user.getHabit().remove(foundHabit);
        monsterService.minusExpWithCount(user, foundHabit.getAccomplishCounter());
        habitRepository.delete(foundHabit);


        return DefaultResponseDto.builder()
                .statusCode(200)
                .responseMessage("Habit Delete Completed")
                .build();
    }

    @Override
    public List<HabitSummaryVo> getHabitSummaryList(User user, LocalDate today) {
        int day = today.getDayOfWeek().getValue();
        List<Habit> habits = habitRepository.findTodayHabitListByUser(user, day, today);
        return HabitSummaryVo.listOf(habits);
    }

    @Override
    @Transactional
    public HabitDetailResponseDto updateHabit(Long habitId, HabitUpdateRequestDto habitUpdateRequestDto, User user) {

        HabitWithCounter habit = habitWithCounterRepository.findById(habitId)
                .orElseThrow(() -> new HabitIdNotFoundException("Not Found Habit"));

        isOwner(user, habit);

        habit.updateHabit(habitUpdateRequestDto);

        HabitDetail build = HabitDetail.of(habit);
        return HabitDetailResponseDto.builder()
                .habit(build)
                .statusCode(200)
                .responseMessage("Habit updated")
                .build();

    }

    public HabitSummaryListResponseDto getHabitSummaryListResponseDto(User user, LocalDate today) {
        return HabitSummaryListResponseDto.builder()
                .habits(getHabitSummaryList(user, today))
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

    private void isOwner(User user, Habit habit) {
        if (!habit.getUser().getId().equals(user.getId())) {
            throw new HasNoPermissionException("User Has No Permissions");
        }
    }

    private HabitDetail buildHabitDetail(Habit habit) {
        HabitDetail habitDetail = HabitDetail.builder()
                .habitId(habit.getId())
                .category(habit.getCategory())
                .count(habit.getGoalInSession())
                .totalCount(habit.getTotalCount())
                .description(habit.getDescription())
                .durationEnd(habit.getDurationEnd().toString())
                .durationStart(habit.getDurationStart().toString())
                .achievePercentage(habit.getAchievePercentage())
                .practiceDays(habit.getPracticeDays())
                .current(habit.getCurrent())
                .categoryId(habit.getCategory().getCategoryId())
                .title(habit.getTitle())
                .isAccomplished(false)
                .build();
        return habitDetail;
    }

    private void plusExpPointAndMakeHistory(Habit habit) {
        monsterService.plusExpPoint(habit.getUser());
        History history = History.makeHistory(habit);
        historyRepository.save(history);
    }

    private void deleteHabitIfCompleteToday(Habit habit, LocalDate today) {
        if (habit.getDurationEnd().equals(today)) {
            CompletedHabit completedHabit = CompletedHabit.of(habit);
            completedHabitRepository.save(completedHabit);
            habitRepository.delete(habit);
        }
    }
}
