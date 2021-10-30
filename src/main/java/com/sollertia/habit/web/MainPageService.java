package com.sollertia.habit.web;

import com.sollertia.habit.domain.avatar.AvatarService;
import com.sollertia.habit.domain.avatar.dto.AvatarVo;
import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.service.habitservice.HabitService;
import com.sollertia.habit.web.dto.MainPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final AvatarService avatarService;
    private final HabitService habitService;

    public MainPageResponseDto getMainPageResponseDto(User user) {
        List<HabitSummaryVo> habitSummaryList =
                habitService.getHabitSummaryVoList(user.getId());
        AvatarVo avatarVo = avatarService.getAvatarVo(user);

        return MainPageResponseDto.builder()
                .habits(habitSummaryList)
                .avatar(avatarVo)
                .expPercentage(user.getExpPoint())
                .responseMessage("메인페이지 조회 성공")
                .statusCode(200)
                .build();
    }
}
