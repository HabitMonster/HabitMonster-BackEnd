package com.sollertia.habit.global.utils;


import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.entity.PreSet;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.service.PreSetServiceImpl;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerRunner {

    private final PreSetRepository preSetRepository;
    private final HabitWithCounterRepository habitWithCounterRepository;
    private final CompletedHabitRepository completedHabitRepository;
    private final PreSetServiceImpl preSetService;
    private final HabitRepository habitRepository;
    private final MonsterRepository monsterRepository;
    private final HistoryRepository historyRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void runWhenEveryMidNight() {
        LocalDate date = LocalDate.now();
        minusExpOnLapsedHabit(date);
        expireHabit(date);
    }

    private void minusExpOnLapsedHabit(LocalDate date) {
        String day = String.valueOf(date.minusDays(1).getDayOfWeek().getValue());
        List<Habit> habitsWithDaysAndAccomplish = habitRepository.findHabitsWithDaysAndAccomplish(day, false);

        for (Habit habit : habitsWithDaysAndAccomplish) {
            minusExpFromOwnerOf(habit);
            makeHistoryOf(habit);
        }
        habitRepository.updateAccomplishInSessionToFalse();
    }

    private void minusExpFromOwnerOf(Habit habit) {
        Monster monster = monsterRepository.findByUserId(habit.getUser().getId()).orElseThrow(
                () -> new MonsterNotFoundException(habit.getUser().getSocialId() + "의 몬스터를 찾을 수 없습니다."));
        monster.minusExpPoint();
        monsterRepository.saveAndFlush(monster);
    }

    private void makeHistoryOf(Habit habit) {
        History history = History.makeHistory(habit);
        historyRepository.save(history);
    }

    private void expireHabit(LocalDate date) {
        List<Habit> habitListForDelete = habitRepository.findAllByDurationEndLessThan(date);

        moveToCompletedHabitList(habitListForDelete);
        deleteHabitList(habitListForDelete);
    }

    private void moveToCompletedHabitList(List<Habit> habitListForDelete) {
        List<CompletedHabit> completedHabitList = CompletedHabit.listOf(habitListForDelete);
        System.out.println(completedHabitList.size()+" completedHabitList");
        completedHabitRepository.saveAll(completedHabitList);
    }

    private void deleteHabitList(List<Habit> habitListForDelete) {
        List<Long> habitIdListForDelete = habitListForDelete.stream().map(Habit::getId).collect(Collectors.toList());
        habitRepository.deleteAllById(habitIdListForDelete);
    }

    @Scheduled(cron = "0 0 1 ? * SUN")
    public void runWhenEveryWeek() {
        makePreset();
    }

    private void makePreset() {

        preSetService.deletePreSet();

        Long userId = completedHabitRepository.maxIsSuccessTrueUser(PageRequest.of(0, 1));
        if (userId == null) {
            userId = 1L;
        }

        List<PreSetVo> habits = habitWithCounterRepository.findByUserId(userId).stream()
                .map(PreSetVo::new).collect(Collectors.toCollection(ArrayList::new));

        if (habits.size() < 15) {
            habits.clear();
            habits = habitWithCounterRepository.findByUserId(1L).stream()
                    .map(PreSetVo::new).collect(Collectors.toCollection(ArrayList::new));
        }

        List<PreSet> preSets = new ArrayList<>();
        for (PreSetVo h : habits) {
            preSets.add(new PreSet(h));
        }

        preSetRepository.saveAll(preSets);
    }

}

