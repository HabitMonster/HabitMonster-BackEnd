package com.sollertia.habit.testdata;

import com.sollertia.habit.config.jwt.JwtTokenProvider;
import com.sollertia.habit.config.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
public class TestToken {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    @ApiOperation("test 토큰 발급")
    @GetMapping("/test/token")
    public JwtResponseDto testToken(){

        User testUser = userRepository.findBySocialId("1234G").orElseThrow(()->
                new IllegalArgumentException("유저 없음"));

        String accessToken = jwtTokenProvider.responseAccessToken(testUser);
        String refreshToken = jwtTokenProvider.responseRefreshToken(testUser);

        return JwtResponseDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();
    }
}
