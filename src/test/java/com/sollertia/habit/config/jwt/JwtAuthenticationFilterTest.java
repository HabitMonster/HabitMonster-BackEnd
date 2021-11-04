package com.sollertia.habit.config.jwt;

import com.sollertia.habit.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisUtil redisUtil;

    @BeforeEach
    private void beforeEach() {
//        String jwtToken = jwtTokenProvider.requestAccessToken(request);
//        String refreshToken = jwtTokenProvider.requestRefreshToken(request);
//        String refreshSocialId;
//        String messageBody;
    }

    @Test
    void doFilterInternal() {


    }
}