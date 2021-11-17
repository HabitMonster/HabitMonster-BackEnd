package com.sollertia.habit.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.userteam.entity.UserTeam;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor //Test용
@Builder //Test용
@Entity
@Getter
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String socialId;

    private String username;

    private String email;

    private boolean disabled;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Habit> habit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MonsterCollection> monsterCollections;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "monster_id", unique = true)
    @JsonIgnore
    private Monster monster;

    public User() {
    }

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

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public static User create(Oauth2UserInfo userInfo) {
        User newUser = new User();
        newUser.setSocialId(userInfo.getId());
        newUser.setEmail(userInfo.getEmail());
        newUser.setUsername(userInfo.getName());
        newUser.setProviderType(userInfo.getProviderType());
        newUser.habit = new ArrayList<>(); //builder 빼면 제거
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
