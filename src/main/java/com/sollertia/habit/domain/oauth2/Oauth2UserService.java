package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.domain.user.UserType;
import com.sollertia.habit.exception.InvalidSocialNameException;
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
        switch (userInfo.getProviderType()){
            case KAKAO: return userRepository.save(User.create(userInfo, UserType.Kakao));
            case NAVER: return userRepository.save(User.create(userInfo, UserType.Naver));
            case GOOGLE: return userRepository.save(User.create(userInfo, UserType.Google));
            default: throw new InvalidSocialNameException("잘못된 소셜 로그인 타입입니다.");
        }
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
