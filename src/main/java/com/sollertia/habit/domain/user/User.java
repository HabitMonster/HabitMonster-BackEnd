package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.monster.MonsterCollection;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.userteam.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor //Test용
@Builder //Test용
@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String socialId;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Habit> habit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MonsterCollection> monsterCollections;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Monster monster;

    public User() {
    }

//    protected User() {  // 이유가 궁금하니다!
//    }

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
}
