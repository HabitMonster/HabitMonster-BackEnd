package com.sollertia.habit.domain.monster.controller;

import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.monster.service.MonsterCollectionService;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequiredArgsConstructor
public class MonsterController {

    private final MonsterService monsterService;
    private final MonsterCollectionService monsterCollectionService;

    @Cacheable(value = "Monsters", key = "1")
    @ApiOperation(value = "LV1 몬스터 목록 조회", notes = "몬스터 목록 반환")
    @GetMapping("/monsters")
    public MonsterListResponseDto getAllMonsters(@ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return monsterService.getAllMonsters(userDetails.getUser());
    }

    @ApiOperation(value = "몬스터 선택", notes = "변경된 몬스터 데이터 반환")
    @PatchMapping("/user/monster")
    public MonsterResponseDto updateMonster(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MonsterSelectRequestDto requestDto) {
        return monsterService.updateMonster(userDetails.getUser(), requestDto);
    }

    @ApiOperation(value = "몬스터 이름 수정", notes = "변경된 몬스터 데이터 반환")
    @PatchMapping("/monster/name")
    public MonsterResponseDto updateMonsterName(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody MonsterSelectRequestDto requestDto) {
        monsterCollectionService.updateMonsterName(userDetails.getUser(), requestDto.getMonsterName());
        return monsterService.updateMonsterName(userDetails.getUser(), requestDto);
    }

    @ApiOperation(value = "몬스터 컬렉션 조회", notes = "사용자 몬스터 컬렉션 목록 반환")
    @GetMapping("/user/monsters")
    public MonsterCollectionResponseDto getMonsterCollection(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return monsterCollectionService.getMonsterCollectionResponseDto(userDetails.getUser());
    }

    @ApiOperation(value = "사용자 몬스터 조회", notes = "사용자 몬스터 정보 반환")
    @GetMapping("/user/monster")
    public MonsterResponseDto getMonsterFromUser(
            @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return monsterService.getMonsterResponseDtoFromUser(userDetails.getUser());
    }
}
