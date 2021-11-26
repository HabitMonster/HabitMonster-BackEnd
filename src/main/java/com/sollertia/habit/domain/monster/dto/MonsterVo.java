package com.sollertia.habit.domain.monster.dto;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.user.dto.UserMonsterVo;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MonsterVo {
    private Long monsterId;
    private Long levelOneId;
    private String monsterImage;
    private String monsterName;
    private Integer monsterLevel;
    private Long monsterExpPoint;
    private String createAt;

    public static MonsterVo of(Monster monster) {
        return MonsterVo.builder()
                .monsterId(monster.getMonsterDatabase().getId())
                .levelOneId(monster.getMonsterDatabase().getMonsterType().getLv1Id())
                .monsterImage(monster.getMonsterDatabase().getImageUrl())
                .monsterName(monster.getName())
                .monsterLevel(monster.getLevel().getValue())
                .monsterExpPoint(monster.getExpPoint())
                .createAt(monster.getCreatedAt().toLocalDate().toString())
                .build();
    }

    public static MonsterVo from(UserMonsterVo userMonsterVo) {
        return MonsterVo.builder()
                .monsterId(userMonsterVo.getMonsterId())
                .levelOneId(userMonsterVo.getLevelOneId())
                .monsterImage(userMonsterVo.getMonsterImage())
                .monsterName(userMonsterVo.getMonsterName())
                .monsterLevel(userMonsterVo.getMonsterLevel())
                .monsterExpPoint(userMonsterVo.getMonsterExpPoint())
                .createAt(userMonsterVo.getCreateAt())
                .build();
    }
}
