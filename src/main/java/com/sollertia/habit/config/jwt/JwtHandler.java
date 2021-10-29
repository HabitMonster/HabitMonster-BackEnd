package com.sollertia.habit.config.jwt;

import com.sollertia.habit.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public String getAccessToken(User user) {
        return jwtTokenProvider.responseAccessToken(user);
    }

    public String getRefreshToken(User user) {
        return jwtTokenProvider.responseRefreshToken(user);
    }

}