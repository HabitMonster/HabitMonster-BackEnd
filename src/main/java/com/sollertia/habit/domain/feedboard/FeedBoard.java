package com.sollertia.habit.domain.feedboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;

@Entity
public class FeedBoard {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
}
