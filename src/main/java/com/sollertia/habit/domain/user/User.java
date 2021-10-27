package com.sollertia.habit.domain.user;

import com.sollertia.habit.domain.avatar.Avatar;
import com.sollertia.habit.domain.avatar.AvatarCollection;
import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.enums.Level;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;
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

    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    private Long expPoint;

    private Level level;

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
}
