package com.sollertia.habit.domain.user.follow.controller;

import com.sollertia.habit.domain.user.follow.dto.FollowCheckDto;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseDto;
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

    @ApiOperation(value = "follower 목록", notes = "follower list")
    @GetMapping("/followers")
    public FollowResponseDto getFollowers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.getFollowers(userDetails.getUser());
    }

    @ApiOperation(value = "following 목록", notes = "following list")
    @GetMapping("/followings")
    public FollowResponseDto getFollowings(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.getFollowings(userDetails.getUser());
    }

    @ApiOperation(value = "follow 요청", notes = "로그인 중인 User와 follo 대상자 매핑")
    @PatchMapping("/follow/{monsterCode}")
    public FollowCheckDto requestFollow(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestFollow(followingId, userDetails.getUser());
    }

    @ApiOperation(value = "unFollow 요청", notes = "로그인 중인 User의 followList에서 unFollow 대상 제거")
    @DeleteMapping("/unFollow/{monsterCode}")
    public FollowCheckDto requestUnFollow(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.requestUnFollow(followingId, userDetails.getUser());
    }

    @ApiOperation(value = "친구 검색", notes = "Following User 검색")
    @GetMapping("/monsterCode/{monsterCode}")
    public FollowSearchResponseDto searchFollowing(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.searchFollowing(followingId, userDetails.getUser());
    }

    @ApiOperation(value = "Check Follow", notes = "Follow 유무 확인")
    @GetMapping("/checkFollow/{monsterCode}")
    public FollowCheckDto checkFollow(@PathVariable(name = "monsterCode") String followingId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return followService.checkFollow(followingId, userDetails.getUser());
    }

}
