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

    @ApiOperation(value = "follower 리스트", notes = "follower list")
    @GetMapping("/followerList")
    public FollowResponseDto getFollowerList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.getFollowerList(userDetails.getUser());
    }

    @ApiOperation(value = "following 리스트", notes = "following list")
    @GetMapping("/followingList")
    public FollowResponseDto getFollowingList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.getFollowingList(userDetails.getUser());
    }

    @ApiOperation(value = "follow 요청", notes = "로그인 중인 User와 follo 대상자 매핑")
    @PatchMapping("/follow/{monsterCode}")
    public DefaultResponseDto requestFollow(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestFollow(followingId, userDetails.getUser());
    }

    @ApiOperation(value = "unFollow 요청", notes = "로그인 중인 User의 followList에서 unFollow 대상 제거")
    @DeleteMapping("/unFollow/{monsterCode}")
    public DefaultResponseDto requestUnFollow(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestUnFollow(followingId, userDetails.getUser());
    }

}
