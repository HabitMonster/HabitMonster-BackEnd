package com.sollertia.habit.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserMonsterVo {

    User user;

    private String monsterCode;
    private String username;
    private String email;
    private Boolean isFollowed;

    private Long monsterId;
    private Long levelOneId;
    private String monsterImage;
    private String monsterName;
    private Integer monsterLevel;
    private Long monsterExpPoint;
    private String createAt;

    @QueryProjection
    public UserMonsterVo(String monsterCode, String username, String email, Long monsterId, MonsterType monsterType, String monsterImage,
                         String monsterName, Level level, Long expPoint, LocalDateTime createAt, Boolean isFollowed, User user){
            this.monsterCode = monsterCode;
            this.username = username;
            this.email = email;
            this.monsterId = monsterId;
            this.levelOneId = monsterType.getLv1Id();
            this.monsterImage = monsterImage;
            this.monsterName = monsterName;
            this.monsterLevel = level.getValue();
            this.monsterExpPoint = expPoint;
            this.createAt = createAt.toLocalDate().toString();
            this.isFollowed = isFollowed;
            this.user = user;
    }
}
