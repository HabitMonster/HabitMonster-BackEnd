package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.OAuthProviderMissMatchException;
import com.sollertia.habit.exception.UserIdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService {

    private final UserRepository userRepository;

    public User loadUser(Oauth2UserInfo userInfo, boolean isFirstLogin) {
        User user = findUserOrCreateNewUser(userInfo, isFirstLogin);
        checkProviderBetween(userInfo, user);
        return updateUserIfChanged(user, userInfo);
    }

    private User findUserOrCreateNewUser(Oauth2UserInfo userInfo, boolean isFirstLogin) {
        User user;
        if ( isFirstLogin ) {
            user = createUser(userInfo);
        } else {
            user = userRepository.findByUserId(userInfo.getId()).orElseThrow(
                    () -> new UserIdNotFoundException("사용자를 찾을 수 없습니다.")
            );
        }
        return user;
    }

    private User createUser(Oauth2UserInfo userInfo) {
        User user = User.create(userInfo);
        return userRepository.save(user);
    }

    private void checkProviderBetween(Oauth2UserInfo userInfo, User user) {
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
        if ( optionalUser.isPresent() ) {
            return false;
        } else {
            return true;
        }
    }
}
