package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.avatar.AvatarCollection;
import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.enums.Level;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.userteam.UserTeam;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String username;

    private String email;

    private String password;

    private ProviderType providerType;

    private Long expPoint;

    private Level level;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Habit> habits;

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

    private void setPassword(String password) {
        this.password = password;
    }

    private void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public void updateUsername(String username) {
        this.setUsername(username);
    }

    public static User create(Oauth2UserInfo userInfo) {
        User newUser = new User();
        newUser.setUserId(userInfo.getId());
        newUser.setEmail(userInfo.getEmail());
        newUser.setUsername(userInfo.getName());
        newUser.setPassword(UUID.randomUUID().toString());
        newUser.setProviderType(userInfo.getProviderType());
        return newUser;
    }
}
