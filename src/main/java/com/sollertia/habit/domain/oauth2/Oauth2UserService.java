package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.OAuthProviderMissMatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class Oauth2UserService {

    private final UserRepository userRepository;

    public Map<String, Object> loadUser(Oauth2UserInfo userInfo) {
        Map<String, Object> userMap = findUserOrCreateNewUser(userInfo);
        checkProviderBetween(userInfo, (User) userMap.get("user"));
        updateUserIfChanged((User) userMap.get("user"), userInfo);
        return userMap;
    }

    private Map<String, Object> findUserOrCreateNewUser(Oauth2UserInfo userInfo) {
        boolean isFirstLogin = true;
        User user;

        Optional<User> optionalUser = userRepository.findByUserId(userInfo.getId());
        if ( optionalUser.isPresent() ) {
            user = optionalUser.get();
            isFirstLogin = false;
        } else {
            user = createUser(userInfo);
        }
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("isFirstLogin", isFirstLogin);
        userMap.put("user", user);
        return userMap;
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
        return !optionalUser.isPresent();
    }
}
