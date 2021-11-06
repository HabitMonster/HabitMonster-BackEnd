package com.sollertia.habit.domain.main.controller;

import com.sollertia.habit.domain.main.dto.MainPageResponseDto;
import com.sollertia.habit.domain.main.service.MainPageService;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainPageService mainPageService;

    @ApiOperation(value = "메인페이지 정보 요청", notes = "몬스터 정보, 습관 리스트, 경험치 비율 반환")
    @GetMapping("/main")
    public MainPageResponseDto getMainPageData(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws Throwable {
        return mainPageService.getMainPageResponseDto(userDetails.getUser());
    }
}
