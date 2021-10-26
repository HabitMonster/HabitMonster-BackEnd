package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.avatar.AvatarCollection;
import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.userteam.UserTeam;

import javax.persistence.*;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String userId;

    private String password;

    private ProviderType providerType;

    private Long expPoint;

    @OneToMany(mappedBy = "user")
    private List<Habit> habits;

    @OneToMany(mappedBy = "user")
    private List<UserTeam> userTeam;

    @OneToMany
    private List<AvatarCollection> avatarCollections;

    @ManyToOne
    private Avatar avatar;
}
