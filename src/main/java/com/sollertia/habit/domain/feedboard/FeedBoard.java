package com.sollertia.habit.domain.feedboard;

import com.sollertia.habit.domain.user.User;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class FeedBoard {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String content;

    @ManyToOne
    private User user;
}
