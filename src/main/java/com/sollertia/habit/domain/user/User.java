package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.avatar.AvatarCollection;
import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.enums.Level;
import com.sollertia.habit.domain.userteam.UserTeam;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String password;

    private ProviderType providerType;

    private Long expPoint;

    private Level level;

    @Enumerated(value = EnumType.STRING)
    private UserType type;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Habit> habits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeam;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AvatarCollection> avatarCollections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Avatar avatar;
}
