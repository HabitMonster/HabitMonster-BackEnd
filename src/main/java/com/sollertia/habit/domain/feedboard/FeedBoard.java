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
    Long id;

    String title;

    String content;

    @ManyToOne
    User user;
}
