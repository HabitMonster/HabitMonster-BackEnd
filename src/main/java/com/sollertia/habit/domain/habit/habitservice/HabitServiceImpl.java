package com.sollertia.habit.domain.habit.habitservice;

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

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;

    private final HabitWithCounterRepository habitWithCounterRepository;

    private final UserRepository userRepository;

    private final HistoryRepository historyRepository;


    @Transactional
    @Override
    public HabitDetailResponseDto createHabit(HabitTypeDto habitTypeDto, HabitDtoImpl createHabitRequestDto, User user) {

        //임시 수정, getGoalCount override해서 형변환 없이 리팩토링 하기
        HabitWithCounter habit = (HabitWithCounter) Habit.createHabit(habitTypeDto.getHabitType(), createHabitRequestDto, user);

        habitRepository.save(habit);

        HabitDetail build = HabitDetail.builder()
                .habitId(habit.getId())
                .category(habit.getCategory().toString())
                .count(habit.getGoalCountInSession())
                .description(habit.getDescription())
                .durationEnd(habit.getDurationEnd().toString())
                .durationStart(habit.getDurationStart().toString())
                .achievePercentage(habit.getAchievePercentage())
                .current(habit.getCurrent())
                .title(habit.getTitle())
                .build();

        return HabitDetailResponseDto.builder().habitDetail(build).statusCode(200).responseMessage("습관 생성 성공").build();

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
                .achievePercentage(foundHabit.getAchievePercentage())
                .current(foundHabit.getCurrent())
                .title(foundHabit.getTitle())
                .build();

        return HabitDetailResponseDto.builder().habitDetail(build).responseMessage("습관 상세 조회 성공").statusCode(200).build();
    }

    @Override
    public HabitCheckResponseDto checkHabit(HabitTypeDto habitTypeDto, Long habitId) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(() -> new HabitIdNotFoundException("Couldn't find Habit"));
        Boolean isAchieve = habitWithCounter.check(1L);
        if (isAchieve) {
            habitWithCounter.getUser().plusExpPoint();
            History history = History.makeHistory(habitWithCounter);
            historyRepository.save(history);
        }
        userRepository.save(habitWithCounter.getUser());
        habitWithCounterRepository.save(habitWithCounter);
        return HabitCheckResponseDto.builder().statusCode(200).responseMessage("성공했습니다").current(habitWithCounter.getCurrent()).isAccomplished(isAchieve).build();

    }

    @Override
    public DefaultResponseDto deleteHabit(HabitTypeDto habitTypeDto, Long habitId, User user) {

        HabitWithCounter habitWithCounter = habitWithCounterRepository.findById(habitId).orElseThrow(() -> new HabitIdNotFoundException("can't find habit"));

        user.getHabit().remove(habitWithCounter);
        habitRepository.delete(habitWithCounter);

        return DefaultResponseDto.builder().statusCode(200).responseMessage("습관 삭제 성공").build();
    }


    @Override
    public List<HabitSummaryVo> getHabitSummaryList(User user) {
        LocalDate today = LocalDate.now();
        int day = today.getDayOfWeek().getValue();
        List<HabitSummaryVo> habitSummaryList = new ArrayList<>();

        List<Habit> habits = habitWithCounterRepository
                .findTodayHabitListByUser(user, day, today);

        for (Habit habit : habits) {
            habitSummaryList.add(HabitSummaryVo.of((HabitWithCounter) habit));
        }
        return habitSummaryList;
    }

    public HabitSummaryListResponseDto getHabitSummaryListResponseDto(User user) {
        return HabitSummaryListResponseDto.builder()
                .habits(getHabitSummaryList(user))
                .responseMessage("사용자 습관 목록 조회 성공")
                .statusCode(200)
                .build();
    }
}
