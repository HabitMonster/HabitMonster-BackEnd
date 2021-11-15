package com.sollertia.habit.domain.user.follow.controller;

import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class FollowController {

    private final FollowServiceImpl followService;

    @ApiOperation(value = "follow 리스트", notes = "follower,following list")
    @GetMapping("/followList")
    public FollowResponseDto getFollowList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.getFollowList(userDetails.getUser());
    }

    @ApiOperation(value = "follow 요청", notes = "로그인 중인 User와 follo 대상자 매핑")
    @PatchMapping("/follow/{following_id}")
    public DefaultResponseDto requestFollow(@PathVariable(name = "following_id") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestFollow(followingId, userDetails.getUser());
    }

    @ApiOperation(value = "unFollow 요청", notes = "로그인 중인 User의 followList에서 unFollow 대상 제거")
    @DeleteMapping("/unFollow/{following_id}")
    public DefaultResponseDto requestUnFollow(@PathVariable(name = "following_id") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestUnFollow(followingId, userDetails.getUser());
    }

}
