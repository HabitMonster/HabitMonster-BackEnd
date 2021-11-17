package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findAllByFollowerId(Long followerId);

    List<Follow> findAllByFollowingId(Long followingId);

    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Follow findByFollowerIdAndFollowingId(Long followerId, Long followingId);

    Integer findCountByFollower(User follower);

    Integer findCountByFollowing(User following);
}
