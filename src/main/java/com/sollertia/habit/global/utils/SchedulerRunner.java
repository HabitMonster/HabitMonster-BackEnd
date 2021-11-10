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

    //@Scheduled(cron = "0 0 1 ? * SUN") // 매주 일요일 새벽 1시
    //    @Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "0 0 1 ? * WED") // 매주 수요일 새벽 1시
    public void runWhenEveryWeek() {
        // 매주 일요일 새벽 1시 CompletedHabit 에서 성공한 습관이 가장 많은 유저가 현재 수행 중인 습관을 등록
        // 등록 전에 이미 등록되어있는 유저의 현재 습관 삭제
        makePreset();
    }

    // 매주 일요일 새벽 1시 마다 현재
    private void makePreset() {
        // 이미 등록되어진 유저의 습관을 삭제
        preSetService.deletePreSet();

        // CompletedHabit에서 isSuccess=true 즉 습관 성공이 가장 많은 유저 가져오기
        Long userId = completedHabitRepository.maxIsSuccessTrueUser(PageRequest.of(0, 1));
        if (userId == null) {
            userId = 1L;
        } // 회원탈퇴 유저일 수도 있기 때문에 admin id 1번 부여
        // 위에서 가져온 유저의 현재 진행 중인 습관을 가져와서 PreSet에 넣기
        List<PreSetVo> habits = habitWithCounterRepository.findByUserId(userId).stream()
                .map(PreSetVo::new).collect(Collectors.toCollection(ArrayList::new));
        // 해당 유저의 현재 진행 중인 습관의 총 개수가 15개 미만이라면 admin 유저 사용
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

