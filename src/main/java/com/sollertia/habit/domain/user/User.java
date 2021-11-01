package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.monster.Monster;
import com.sollertia.habit.domain.monster.MonsterCollection;
import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;
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

    private Long expPoint;

    private Level level;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    private String monsterName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HabitWithCounter> habitsWithCounter;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HabitWithTimer> habitsWithTimer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<MonsterCollection> monsterCollections;

    @ManyToOne(fetch = FetchType.LAZY)
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

    private void setType(UserType type){this.type = type;}

    private void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    private void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public static User create(Oauth2UserInfo userInfo, UserType type) {
        User newUser = new User();
        newUser.setSocialId(userInfo.getId());
        newUser.setEmail(userInfo.getEmail());
        newUser.setUsername(userInfo.getName());
        newUser.setProviderType(userInfo.getProviderType());
        newUser.setType(type);
        return newUser;
    }

    public void plusExpPoint() {
        this.expPoint += this.level.getPlusPoint();
    }

    public void updateMonster(Monster monster, String monsterName) {
        this.setMonster(monster);
        this.setMonsterName(monsterName);
    }
}
