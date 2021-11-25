package com.sollertia.habit.domain.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
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

    private  UserDetailsVo userInfo;
    private  MonsterVo monster;


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

    public UserMonsterVo of(UserMonsterVo userMonsterVo, FollowCount followCount, Integer totalHabitCount){
        this.userInfo = UserDetailsVo.builder()
                .monsterCode(userMonsterVo.getMonsterCode())
                .username(userMonsterVo.getUsername())
                .email(userMonsterVo.getEmail())
                .isFollowed(userMonsterVo.getIsFollowed())
                .totalHabitCount(totalHabitCount)
                .followersCount(followCount.getFollowersCount())
                .followingsCount(followCount.getFollowingsCount())
                .build();

        this.monster = MonsterVo.builder()
                .monsterId(userMonsterVo.getMonsterId())
                .levelOneId(userMonsterVo.getLevelOneId())
                .monsterImage(userMonsterVo.getMonsterImage())
                .monsterName(userMonsterVo.getMonsterName())
                .monsterLevel(userMonsterVo.getMonsterLevel())
                .monsterExpPoint(userMonsterVo.getMonsterExpPoint())
                .createAt(userMonsterVo.getCreateAt())
                .build();

        return this;
    }


}
