package com.sollertia.habit.web;

import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.service.habitservice.HabitServiceImpl;
import com.sollertia.habit.utils.DefaultResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController("/habits")
@RequiredArgsConstructor
public class HabitController {

    private final HabitServiceImpl habitService;

    @PostMapping("/")
    public DefaultResponseDto createHabit(@RequestBody HabitDtoImpl habitDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        habitDto.setUser(userDetails.getUser());

        return habitService.createHabit(habitTypeDto, habitDto);
    }

    @GetMapping("/{habitId}")
    public HabitDetailResponseDto habitDetailResponseDto(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.getHabitDetail(habitTypeDto, habitId);
    }

    @ApiOperation(value = "습관 삭제", notes = "성공 실패여부 반환")
    @DeleteMapping("/{habitId}")
    public DefaultResponseDto deleteHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.deleteHabit(habitTypeDto, habitId);
    }

    @GetMapping("/check/{habitId}")
    public DefaultResponseDto checkHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.checkHabit(habitTypeDto, habitId);
    }
}
