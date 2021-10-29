package com.sollertia.habit.web;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.service.habitservice.HabitServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitServiceImpl habitService;

    @PostMapping("/")
    public ResponseDto createHabit(@RequestBody HabitDtoImpl habitDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "nPerDay");

        habitDto.setUser(userDetails.getUser());

        Habit habit = habitService.createHabit(habitTypeDto, habitDto);

        return new ResponseDto(200L, "성공했습니다.");
    }

    @GetMapping("/{habitId}")
    public HabitDetailResponseDto habitDetailResponseDto(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "nPerDay");

        HabitDetail habitDetail = habitService.getHabitDetail(habitTypeDto, habitId);

        return new HabitDetailResponseDto(habitDetail, 200L, "성공");
    }

    @DeleteMapping("/{habitId}")
    public ResponseDto deleteHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        //예외처리 핸들러로 로그인 여부 예외처리 진행

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "nPerDay");

        ResponseDto responseDto = habitService.deleteHabit(habitTypeDto, habitId);

        return responseDto;
    }

    @GetMapping("/check/{habitId}")
    public ResponseDto checkHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        //예외처리 핸들러로 로그인 여부 예외처리 진행
        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "nPerDay");

        ResponseDto responseDto = habitService.checkHabit(habitTypeDto, habitId);

        return responseDto;
    }
}
