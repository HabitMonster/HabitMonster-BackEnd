package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.avatar.AvatarCollection;
import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.userteam.UserTeam;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private Long expPoint;

    private Level level;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    private String avatarName;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HabitWithCounter> habitsWithCounter;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<HabitWithTimer> habitsWithTimer;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AvatarCollection> avatarCollections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;

    protected User() {
    }

    private void setUserId(String userId) {
        this.userId = userId;
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

    private void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    private void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public static User create(Oauth2UserInfo userInfo, UserType type) {
        User newUser = new User();
        newUser.setUserId(userInfo.getId());
        newUser.setEmail(userInfo.getEmail());
        newUser.setUsername(userInfo.getName());
        newUser.setProviderType(userInfo.getProviderType());
        newUser.setType(type);
        return newUser;
    }

    public void plusExpPoint() {
        this.expPoint += this.level.getPlusPoint();
    }

    public void selectAvatar(Avatar avatar, String avatarName) {
        this.setAvatar(avatar);
        this.setAvatarName(avatarName);
    }
}
