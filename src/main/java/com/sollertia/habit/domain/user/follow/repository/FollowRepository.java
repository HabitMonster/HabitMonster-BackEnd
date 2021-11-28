package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    void deleteByFollower(User follower);

    void deleteByFollowing(User following);

    void deleteAllByFollowingIn(Collection<User> following);

    void deleteAllByFollowerIn(Collection<User> follower);
}
