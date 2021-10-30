package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitFactory;
import com.sollertia.habit.domain.habit.Repository.HabitCounterRepository;
import com.sollertia.habit.domain.habit.Repository.HabitTimerRepository;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.history.History;
import com.sollertia.habit.domain.history.HistoryRepository;
import com.sollertia.habit.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class HabitServiceImpl implements HabitService {

    private final Map<HabitType, JpaRepository> repositories;
    private final UserRepository userRepository;
    private final HistoryRepository historyRepository;

    @Autowired
    public HabitServiceImpl(HabitCounterRepository habitCounterRepository,
                            HabitTimerRepository habitTimerRepository,
                            UserRepository userRepository,
                            HistoryRepository historyRepository) {

        Map<HabitType, JpaRepository> repositories = new HashMap<>();

        repositories.put(HabitType.HABITWITHCOUNTER, habitCounterRepository);

        repositories.put(HabitType.HABITWITHTIMER, habitTimerRepository);

        this.repositories = repositories;
        this.userRepository = userRepository;
        this.historyRepository = historyRepository;
    }


    @Override
    public Habit createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto) {

        Habit habit = HabitFactory.createHabit(habitTypeDto, createHabitRequestDto);

        Habit save = (Habit)repositories.get(habitTypeDto.getHabitType()).save(habit);

        return habit;

    }

    @Override
    public HabitDetail getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) {

        Habit foundHabit = null;
        try {
            foundHabit = (Habit)repositories
                    .get(habitTypeDto.getHabitType())
                    .findById(habitId).orElseThrow(()-> new IllegalArgumentException("habit not found execption"));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        HabitDetail build = HabitDetail.builder()
                .habitId(foundHabit.getId())
                .category(foundHabit.getCategory().toString())
                .count(foundHabit.getCurrent())
                .description(foundHabit.getDescription())
                .durationEnd(foundHabit.getDurationEnd().toString())
                .durationStart(foundHabit.getDurationStart().toString())
                .sessionDuration(foundHabit.getSessionDuration())
                .title(foundHabit.getTitle())
                .build();

        return build;
    }

    @Override
    public ResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId) {

        Habit habit = (Habit) repositories.get(habitTypeDto.getHabitType()).findById(habitId).get();
        Boolean isAchieve = habit.check(1L);
        if (isAchieve) {
            habit.getUser().plusExpPoint();
            History history = History.makeHistory(habit);
            historyRepository.save(history);
        }
        userRepository.save(habit.getUser());
        repositories.get(habitTypeDto.getHabitType()).save(habit);
        return new ResponseDto(200L, "체크 성공");
        //성공 여부, 오늘 수행 횟수, 만약 이번 체크로 수행완료했다면 얻게된 경험치 등 반환 가능.


    }

    @Override
    public ResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId) {
        repositories.get(habitTypeDto.getHabitType()).delete(habitId);
        return new ResponseDto(200L, "삭제되었습니다.");
    }


    @Override
    public List<HabitSummaryVo> getHabitSummaryVoList(Long userId) {
        Collection<JpaRepository> values = repositories.values();
        List<HabitSummaryVo> summaryVoList = new ArrayList<>();

        for (JpaRepository value : values) {
            List<Habit> habits = value.findAllByUserId(userId);
            summaryVoList.addAll(HabitSummaryVo.listOf(habits));
        }

        return summaryVoList;
    }

}
