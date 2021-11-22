package com.sollertia.habit.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    private String monsterCode;

    private String username;

    private String email;

    private boolean disabled;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Habit> habit = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> histories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MonsterCollection> monsterCollections = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "monster_id", unique = true)
    @JsonIgnore
    private Monster monster;

    private void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    private void setEmail(String email) {
        this.email = email;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    private void setMonster(Monster monster) {
        this.monster = monster;
    }

    private void setDisabled (boolean disabled) {
        this.disabled = disabled;
    }

    public void setMonsterCode(String monsterCode) {
        this.monsterCode = monsterCode;
    }

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public static User create(Oauth2UserInfo userInfo) {
        User newUser = new User();
        newUser.setSocialId(userInfo.getId());
        newUser.setEmail(userInfo.getEmail());
        newUser.setUsername(userInfo.getName());
        newUser.setProviderType(userInfo.getProviderType());
        return newUser;
    }

    public User updateMonster(Monster monster) {
        this.setMonster(monster);
        monster.setUser(this);
        return this;
    }

    public void toDisabled() {
        this.setDisabled(true);
    }
}
