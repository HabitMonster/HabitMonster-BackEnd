package com.sollertia.habit.web;

import com.sollertia.habit.domain.user.UserDetailsImpl;
import com.sollertia.habit.web.dto.MainPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final MainPageService mainPageService;

    @GetMapping("/")
    public MainPageResponseDto getMainPageData(@AuthenticationPrincipal UserDetailsImpl userDetails) throws Throwable {
        return mainPageService.getMainPageResponseDto(userDetails.getUser());
    }
}
