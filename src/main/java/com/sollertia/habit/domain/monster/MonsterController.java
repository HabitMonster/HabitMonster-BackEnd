package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.user.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class MonsterController {

    private final MonsterService monsterService;

    @ApiOperation(value = "LV1 몬스터 목록 조회", notes = "몬스터 목록 반환")
    @GetMapping("/monsters")
    public MonsterListResponseDto getAllMonsters(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return monsterService.getAllMonsters(userDetails.getUser());
    }

    @ApiOperation(value = "몬스터 변경", notes = "변경된 몬스터 데이터 반환")
    @PatchMapping("/user/monster")
    public MonsterResponseDto updateMonster(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MonsterSelectRequestDto requestDto) {
        return monsterService.updateMonster(userDetails.getUser(), requestDto);
    }

    @ApiOperation(value = "몬스터 컬렉션 조회", notes = "사용자 몬스터 컬렉션 목록 반환")
    @GetMapping("/user/monsters")
    public MonsterListResponseDto getMonsterCollection(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return monsterService.getMonsterCollection(userDetails.getUser());
    }
}
