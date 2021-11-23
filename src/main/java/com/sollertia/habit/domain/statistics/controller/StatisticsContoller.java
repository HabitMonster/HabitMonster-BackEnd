package com.sollertia.habit.domain.statistics.controller;

import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.statistics.service.StatisticsServiceImpl;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StatisticsContoller {

    private final StatisticsServiceImpl statisticsService;

    @ApiOperation(value = "통계 페이지", notes = "기본 통계 정보 반환")
    @GetMapping("/statistics")
    private StatisticsResponseDto getStatistics(@RequestParam String date, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return statisticsService.getStatistics(userDetails.getUser(), date);

    }


}
