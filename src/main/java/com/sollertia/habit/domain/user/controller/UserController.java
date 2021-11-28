package com.sollertia.habit.domain.user.controller;

import com.sollertia.habit.domain.user.dto.RecommendedUserListDto;
import com.sollertia.habit.domain.user.dto.UserDetailResponseDto;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.dto.UsernameUpdateRequestDto;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.domain.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

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

    @ApiOperation(value = "사용자 이름 변경", notes = "새로운 사용자 정보 반환")
    @PatchMapping("/user/name")
    public UserInfoResponseDto updateUsername(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UsernameUpdateRequestDto requestDto
    ) {
        return userService.updateUsername(userDetails.getUser(), requestDto);
    }

    @ApiOperation(value = "회원 탈퇴", notes = "회원 탈퇴 정보 응답")
    @DeleteMapping("/user")
    public UserInfoResponseDto disableUser(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userService.disableUser(userDetails.getUser());
    }

    @ApiOperation(value = "특정 유저 정보 조회", notes = "사용자, 몬스터, 습관 정보 응답")
    @GetMapping("/user/{monsterCode}/info")
    public UserDetailResponseDto getUserInfoByMonsterCode(
            @PathVariable String monsterCode,
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userService.getUserDetailDtoByMonsterCode(userDetails.getUser(), monsterCode);
    }

    @ApiOperation(value = "추천 유저 정보 조회", notes = "사용자, 몬스터, 습관 정보 응답")
    @GetMapping("/users/recommended")
    public RecommendedUserListDto getRecommendedUsers(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return userService.getRecommendedUserListDto(userDetails.getUser());
    }
}
