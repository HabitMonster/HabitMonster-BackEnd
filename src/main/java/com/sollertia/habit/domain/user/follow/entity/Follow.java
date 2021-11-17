package com.sollertia.habit.domain.user.follow.entity;

import com.sollertia.habit.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private User following;

    private void setFollower(User follower) {
        this.follower = follower;
    }

    private void setFollowing(User following) {
        this.following = following;
    }

    public static Follow create(User follower, User following){
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        return follow;
    }

}
