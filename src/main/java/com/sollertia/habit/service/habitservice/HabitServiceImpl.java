package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitFactory;
import com.sollertia.habit.domain.habit.Repository.*;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.dto.ResponseDto;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class HabitServiceImpl implements HabitService {

    private final Map<HabitType, JpaRepository> repositories;
    private final UserRepository userRepository;

    @Autowired
    public HabitServiceImpl(HabitCounterRepository habitCounterRepository,
                            HabitTimerRepository habitTimerRepository,
                            UserRepository userRepository) {

        Map<HabitType, JpaRepository> repositories = new HashMap<>();

        repositories.put(HabitType.HABITWITHCOUNTER, habitCounterRepository);

        repositories.put(HabitType.HABITWITHTIMER, habitTimerRepository);

        this.repositories = repositories;
        this.userRepository = userRepository;
    }


    @Override
    public Habit createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto) {

        Habit habit = HabitFactory.createHabit(habitTypeDto, createHabitRequestDto);

        Habit save = (Habit)repositories.get(habitTypeDto.getHabitType()).save(habit);

        return habit;

    }

    @Override
    public HabitDetail getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) throws Throwable {

        Habit foundHabit = (Habit) repositories.get(habitTypeDto.getHabitType()).findById(habitId).get();

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
        }
        userRepository.save(habit.getUser());
        //성공 여부, 오늘 수행 횟수, 만약 이번 체크로 수행완료했다면 얻게된 경험치 등 반환 가능.
        return new ResponseDto(200L, "성공했습니다.");
    }

    @Override
    public ResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId) {
        repositories.get(habitTypeDto.getHabitType()).delete(habitId);
        return new ResponseDto(200L, "삭제되었습니다.");
    }

}
