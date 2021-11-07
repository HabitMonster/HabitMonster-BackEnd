package com.sollertia.habit.domain.user.controller;

import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.domain.user.service.UserService;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "사용자 정보 조회", notes = "로그인한 사용자 정보 반환")
    @GetMapping("/user/info")
    public UserInfoResponseDto getUserInfo(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userService.getUserInfoResponseDto(userDetails.getUser());
    }
}
