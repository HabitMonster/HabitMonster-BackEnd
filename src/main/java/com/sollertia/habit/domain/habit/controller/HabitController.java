package com.sollertia.habit.domain.habit.controller;


import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitServiceImpl habitService;

    @ApiOperation(value = "습관 생성", notes = "성공 실패여부 반환")
    @PostMapping("/habits")
    public HabitDetailResponseDto createHabit(final @Valid @RequestBody HabitDtoImpl habitDto,
                                              @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {


        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.createHabit(habitTypeDto, habitDto, userDetails.getUser());
    }

    @ApiOperation(value = "습관 상세정보 요청", notes = "성공 실패여부 반환")
    @GetMapping("/habits/{habitId}")
    public HabitDetailResponseDto habitDetailResponseDto(@PathVariable Long habitId) {


        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.getHabitDetail(habitTypeDto, habitId);
    }

    @ApiOperation(value = "습관 변경", notes = "title, description, count 변경 가능")
    @PatchMapping("/habits/{habitId}")
    public HabitDetailResponseDto updateHabit(@PathVariable Long habitId,
                                              final @Valid @RequestBody HabitUpdateRequestDto habitUpdateRequestDto,
                                              @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {


        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
        return habitService.updateHabit(habitTypeDto, habitId, habitUpdateRequestDto, userDetails.getUser());
    }

    @ApiOperation(value = "습관 삭제", notes = "성공 실패여부 반환")
    @DeleteMapping("/habits/{habitId}")
    public DefaultResponseDto deleteHabit(@PathVariable Long habitId,
                                          @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.deleteHabit(habitTypeDto, habitId, userDetails.getUser());
    }

    @ApiOperation(value = "습관 체크", notes = "성공 실패여부 반환")
    @GetMapping("/habits/check/{habitId}")
    public HabitCheckResponseDto checkHabit(@PathVariable Long habitId) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
        LocalDate today = LocalDate.now();
        return habitService.checkHabit(habitTypeDto, habitId, today);
    }

    @ApiOperation(value = "사용자 습관 목록 조회", notes = "사용자의 오늘의 습관 목록 반환")
    @GetMapping("/user/habits")
    public HabitSummaryListResponseDto getHabitSummaryList(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        LocalDate today = LocalDate.now();
        return habitService.getHabitSummaryListResponseDto(userDetails.getUser(), today);
    }

}
