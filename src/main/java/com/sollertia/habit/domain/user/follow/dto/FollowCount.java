package com.sollertia.habit.domain.user.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowCount {
    private Long followersCount;
    private Long followingsCount;
}
