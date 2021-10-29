package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.OAuthProviderMissMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService {

    private final UserRepository userRepository;

    public Oauth2UserInfo putUserInto(Oauth2UserInfo userInfo) {
        Oauth2UserInfo updatedUserInfo = updateUserInfo(userInfo);
        checkProviderBetween(updatedUserInfo.getUser(), userInfo);
        updateUserIfChanged(updatedUserInfo.getUser(), userInfo);
        return userInfo;
    }

    private Oauth2UserInfo updateUserInfo(Oauth2UserInfo userInfo) {
        Optional<User> optionalUser = userRepository.findByUserId(userInfo.getId());
        if ( optionalUser.isPresent() ) {
            userInfo.putUser(optionalUser.get());
        } else {
            userInfo.putUser(createUser(userInfo));
            userInfo.toFirstLogin();
        }
        return userInfo;
    }

    private User createUser(Oauth2UserInfo userInfo) {
        User user = User.create(userInfo);
        return userRepository.save(user);
    }

    private void checkProviderBetween(User user, Oauth2UserInfo userInfo) {
        if (userInfo.getProviderType() != user.getProviderType()) {
            throw new OAuthProviderMissMatchException(
                    "Looks like you're signed up with " + userInfo.getProviderType() +
                            " account. Please use your " + user.getProviderType() + " account to login."
            );
        }
    }

    private User updateUserIfChanged(User user, Oauth2UserInfo userInfo) {
        if (userInfo.getName() != null && !user.getUsername().equals(userInfo.getName())) {
            user.updateUsername(userInfo.getName());
        }
        return userRepository.save(user);
    }

    public boolean isFirstLogin(Oauth2UserInfo userInfo) {
        Optional<User> optionalUser = userRepository.findByUserId(userInfo.getId());
        return !optionalUser.isPresent();
    }
}
