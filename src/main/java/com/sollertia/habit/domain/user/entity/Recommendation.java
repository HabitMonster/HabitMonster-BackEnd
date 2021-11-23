package com.sollertia.habit.domain.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Recommendation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
