package com.sollertia.habit.web;

import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.service.mainpage.MainPageService;
import com.sollertia.habit.web.dto.MainPageResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MainPageService mainPageService;

    @ApiOperation(value = "메인페이지 정보 요청", notes = "몬스터 정보, 습관 리스트, 경험치 비율 반환")
    @GetMapping("/main")
    public MainPageResponseDto getMainPageData(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) throws Throwable {
        return mainPageService.getMainPageResponseDto(userDetails.getUser());
    }
}
