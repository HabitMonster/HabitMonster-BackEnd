package com.sollertia.habit.domain.avatar;

import com.sollertia.habit.domain.avatar.dto.AvatarListResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarResponseDto;
import com.sollertia.habit.domain.avatar.dto.AvatarSelectRequestDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AvatarController {

    private AvatarService avatarService;

    @GetMapping("/avatars")
    public AvatarListResponseDto getAllAvatars() {
        return avatarService.getAllAvatars();
    }

    @PostMapping("/avatars/select")
    public AvatarResponseDto selectAvatar(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AvatarSelectRequestDto requestDto) {
        return avatarService.selectAvatar(userDetails, requestDto);
    }
}
