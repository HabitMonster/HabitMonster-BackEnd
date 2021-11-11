package com.sollertia.habit.web;

import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.monster.MonsterService;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.habit.habitservice.HabitService;
import com.sollertia.habit.web.dto.MainPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainPageService {

    private final MonsterService monsterService;
    private final HabitService habitService;

    public MainPageResponseDto getMainPageResponseDto(User user) throws Throwable {
        List<HabitSummaryVo> habitSummaryList =
                habitService.getHabitSummaryList(user);
        MonsterVo monsterVo = monsterService.getMonsterVo(user);

        return MainPageResponseDto.builder()
                .habits(habitSummaryList)
                .monster(monsterVo)
                .expPercentage(monsterVo.getMonsterExpPoint())
                .responseMessage("메인페이지 조회 성공")
                .statusCode(200)
                .build();
    }
}
