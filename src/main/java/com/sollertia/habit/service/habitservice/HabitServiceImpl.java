package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitFactory;
import com.sollertia.habit.domain.habit.Repository.*;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.dto.ResponseDto;
import com.sollertia.habit.domain.habit.enums.HabitType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class HabitServiceImpl implements HabitService {

    private final Map<HabitType, JpaRepository> repositories;

    @Autowired
    public HabitServiceImpl(HabitCounterRepository habitCounterRepository,
                            HabitTimerRepository habitTimerRepository) {

        Map<HabitType, JpaRepository> repositories = new HashMap<>();

        repositories.put(HabitType.HABITWITHCOUNTER, habitCounterRepository);

        repositories.put(HabitType.HABITWITHTIMER, habitTimerRepository);

        this.repositories = repositories;
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
        //Boolean isAchieve = habit.plusvalue(Long 1L);
        //if(isAchieve){
        //  userRepository.findbyid(habit.getuser.getid);
        //  habit.getUser.plusExp();
        //  userRepository.save(user)
        //}
        //
        //

        return null;
    }

    @Override
    public ResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId) {
        return null;
    }

}
