package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.domain.habit.habitservice.HabitServiceImpl;
import com.sollertia.habit.utils.DefaultResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class HabitController {

    private final HabitServiceImpl habitService;

    @ApiOperation(value = "습관 생성", notes = "성공 실패여부 반환")
    @PostMapping("/habits")
    public DefaultResponseDto createHabit(@RequestBody HabitDtoImpl habitDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.createHabit(habitTypeDto, habitDto, userDetails.getUser());
    }
    @ApiOperation(value = "습관 상세정보 요청", notes = "성공 실패여부 반환")
    @GetMapping("/habits/{habitId}")
    public HabitDetailResponseDto habitDetailResponseDto(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {


        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.getHabitDetail(habitTypeDto, habitId);
    }

    @ApiOperation(value = "습관 삭제", notes = "성공 실패여부 반환")
    @DeleteMapping("/habits/{habitId}")
    public DefaultResponseDto deleteHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.deleteHabit(habitTypeDto, habitId, userDetails.getUser());
    }
    @ApiOperation(value = "습관 체크", notes = "성공 실패여부 반환")
    @GetMapping("/habits/check/{habitId}")
    public DefaultResponseDto checkHabit(@PathVariable Long habitId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");

        return habitService.checkHabit(habitTypeDto, habitId);
    }

    @ApiOperation(value = "사용자 습관 목록 조회", notes = "사용자의 오늘의 습관 목록 반환")
    @GetMapping("/user/habits")
    public HabitSummaryListResponseDto getHabitSummaryList(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return habitService.getHabitSummaryListResponseDto(userDetails.getUser());
    }

}
