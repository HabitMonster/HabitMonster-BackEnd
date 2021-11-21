package com.sollertia.habit.domain.user.oauth2.service;


import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.recommendation.entity.Recommendation;
import com.sollertia.habit.domain.user.recommendation.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.OAuthProviderMissMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final RecommendationRepository recommendationRepository;

    @Transactional
    public Oauth2UserInfo putUserInto(Oauth2UserInfo userInfo) {
        Oauth2UserInfo updatedUserInfo = updateUserInfo(userInfo);
        checkProviderBetween(updatedUserInfo.getUser(), userInfo);
        return userInfo;
    }

    private Oauth2UserInfo updateUserInfo(Oauth2UserInfo userInfo) {
        Optional<User> optionalUser = userRepository.findBySocialId(userInfo.getId());
        if ( optionalUser.isPresent() && !optionalUser.get().isDisabled() ) {
            User user = optionalUser.get();
            if ( user.getMonster() == null ) {
                userInfo.toFirstLogin();
            }
            userInfo.putUser(user);
        } else if ( optionalUser.isPresent() && optionalUser.get().isDisabled() ) {
            deleteUser(optionalUser.get());
            userInfo.putUser(createUser(userInfo));
            userInfo.toFirstLogin();
        } else {
            userInfo.putUser(createUser(userInfo));
            userInfo.toFirstLogin();
        }
        return userInfo;
    }

    private void deleteUser(User user) {
        followRepository.deleteByFollower(user);
        followRepository.deleteByFollowing(user);
        userRepository.delete(user);
    }

    private User createUser(Oauth2UserInfo userInfo) {
        User savedUser = userRepository.save(User.create(userInfo));
        savedUser.setMonsterCode(savedUser.getSocialId().substring(0,5)+savedUser.getId());
        return userRepository.save(savedUser);
    }

    private void checkProviderBetween(User user, Oauth2UserInfo userInfo) {
        if (userInfo.getProviderType() != user.getProviderType()) {
            throw new OAuthProviderMissMatchException(
                    "Looks like you're signed up with " + userInfo.getProviderType() +
                            " account. Please use your " + user.getProviderType() + " account to login."
            );
        }
    }
}
