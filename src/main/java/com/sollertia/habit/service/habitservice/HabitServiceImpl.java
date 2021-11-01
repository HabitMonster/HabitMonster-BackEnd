package com.sollertia.habit.service.habitservice;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.HabitWithCounter;
import com.sollertia.habit.domain.habit.Repository.HabitRepository;
import com.sollertia.habit.domain.habit.Repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.history.History;
import com.sollertia.habit.domain.history.HistoryRepository;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.HabitIdNotFoundException;
import com.sollertia.habit.utils.DefaultResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;

    private final HabitWithCounterRepository habitWithCounterRepository;

    private final UserRepository userRepository;

    private final HistoryRepository historyRepository;


    @Override
    public DefaultResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto) {


        Habit habit = Habit.createHabit(habitTypeDto.getHabitType(), createHabitRequestDto);

        habitRepository.save(habit);

        return DefaultResponseDto.builder().statusCode(200).responseMessage("Save Completed").build();

    }

    @Override
    public HabitDetailResponseDto getHabitDetail(HabitTypeDto habitTypeDto, Long habitId) {

        HabitWithCounter foundHabit = habitWithCounterRepository.findById(habitId).orElseThrow(() -> new HabitIdNotFoundException("Couldn't found Habit"));

        HabitDetail build = HabitDetail.builder()
                .habitId(foundHabit.getId())
                .category(foundHabit.getCategory().toString())
                .count(foundHabit.getGoalCountInSession())
                .description(foundHabit.getDescription())
                .durationEnd(foundHabit.getDurationEnd().toString())
                .durationStart(foundHabit.getDurationStart().toString())
                .sessionDuration(foundHabit.getSessionDuration())
                .title(foundHabit.getTitle())
                .build();

        return HabitDetailResponseDto.builder().habitDetail(build).responseMessage("success").statusCode(200).build();
    }

    @Override
    public DefaultResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(() -> new HabitIdNotFoundException("Couldn't find Habit"));
        Boolean isAchieve = habitWithCounter.check(1L);
        if (isAchieve) {
            habitWithCounter.getUser().plusExpPoint();
            History history = History.makeHistory(habitWithCounter);
            historyRepository.save(history);
        }
        userRepository.save(habitWithCounter.getUser());
        habitWithCounterRepository.save(habitWithCounter);
        return DefaultResponseDto.builder().statusCode(200).responseMessage("success").build();

    }

    @Override
    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId) {

        habitWithCounterRepository.deleteById(habitId);

        return DefaultResponseDto.builder().statusCode(200).responseMessage("success").build();
    }


    @Override
    public List<HabitSummaryVo> getHabitSummaryList(User user) {
        List<HabitSummaryVo> habitSummaryList = new ArrayList<>();
        List<Habit> habits = habitWithCounterRepository.findByUser(user);
        for (Habit habit : habits) {
            habitSummaryList.add(HabitSummaryVo.of((HabitWithCounter) habit));
        }
        return habitSummaryList;
    }

}
