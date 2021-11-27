package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.user.dto.UserMonsterDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonsterDto {
    private Long monsterId;
    private Long levelOneId;
    private String monsterImage;
    private String monsterName;
    private Integer monsterLevel;
    private Long monsterExpPoint;
    private String createAt;

    public static MonsterDto of(Monster monster) {
        return MonsterDto.builder()
                .monsterId(monster.getMonsterDatabase().getId())
                .levelOneId(monster.getMonsterDatabase().getMonsterType().getLv1Id())
                .monsterImage(monster.getMonsterDatabase().getImageUrl())
                .monsterName(monster.getName())
                .monsterLevel(monster.getLevel().getValue())
                .monsterExpPoint(monster.getExpPoint())
                .createAt(monster.getCreatedAt().toLocalDate().toString())
                .build();
    }

    public static MonsterDto from(UserMonsterDto userMonsterDto) {
        return MonsterDto.builder()
                .monsterId(userMonsterDto.getMonsterId())
                .levelOneId(userMonsterDto.getLevelOneId())
                .monsterImage(userMonsterDto.getMonsterImage())
                .monsterName(userMonsterDto.getMonsterName())
                .monsterLevel(userMonsterDto.getMonsterLevel())
                .monsterExpPoint(userMonsterDto.getMonsterExpPoint())
                .createAt(userMonsterDto.getCreateAt())
                .build();
    }
}
