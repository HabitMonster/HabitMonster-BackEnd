package com.sollertia.habit.domain.avatar;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class AvatarCollection {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "avatar_id")
    @JsonIgnore
    private Avatar avatar;
}
