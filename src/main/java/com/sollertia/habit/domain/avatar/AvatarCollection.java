package com.sollertia.habit.domain.avatar;

import com.sollertia.habit.domain.user.User;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class AvatarCollection {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Avatar avatar;

    private String avatarName;

    protected AvatarCollection() {
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    private void setAvatarName(String avatarName) {
        this.avatarName = avatarName;
    }

    public static AvatarCollection createAvatarCollection(User user, Avatar avatar) {
        AvatarCollection avatarCollection = new AvatarCollection();
        avatarCollection.setAvatar(avatar);
        avatarCollection.setUser(user);
        avatarCollection.setAvatarName(user.getAvatarName());
        return avatarCollection;
    }
}
