package com.sollertia.habit.domain.avatar;

import com.sollertia.habit.domain.avatar.dto.AvatarListResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarSelectRequestDto;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AvatarController {

    private final AvatarService avatarService;

    @ApiOperation(value = "LV1 아바타 목록 조회")
    @GetMapping("/avatars")
    public AvatarListResponseDto getAllAvatars() {
        return avatarService.getAllAvatars();
    }

    @ApiOperation(value = "아바타 변경")
    @PostMapping("/avatars/select")
    public AvatarResponseDto selectAvatar(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody AvatarSelectRequestDto requestDto) {
        return avatarService.selectAvatar(userDetails.getUser(), requestDto);
    }
}
