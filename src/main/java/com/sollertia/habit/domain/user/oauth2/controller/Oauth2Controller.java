package com.sollertia.habit.domain.user.oauth2.controller;

import com.sollertia.habit.domain.user.oauth2.dto.CodeDto;
import com.sollertia.habit.domain.user.oauth2.service.Oauth2UserService;
import com.sollertia.habit.domain.user.oauth2.service.SocialLoginService;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {

    private final Oauth2UserService oauth2UserService;
    private final SocialLoginService socialLoginService;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @GetMapping("/user/login/{socialName}")
    public JwtResponseDto login(@RequestParam(value = "code") String authCode,
                                                @PathVariable String socialName) {

        Oauth2UserInfo userInfo = socialLoginService.getUserInfo(socialName, authCode);
        userInfo = oauth2UserService.putUserInto(userInfo);
        User user = userInfo.getUser();

        String accessToken =  jwtTokenProvider.responseAccessToken(user);
        String refreshToken = jwtTokenProvider.responseRefreshToken(user);
        redisUtil.setDataExpire(refreshToken, user.getSocialId(), JwtTokenProvider.REFRESH_TOKEN_USETIME / 1000L);

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .createdAt(user.getCreatedAt().toLocalDate().toString())
                .isFirstLogin(userInfo.isFirstLogin())
                .refreshToken(refreshToken)
                .statusCode(200)
                .responseMessage("Issuance completed Token")
                .build();
    }

    @PostMapping("/user/login/{socialName}")
    public JwtResponseDto login(@RequestBody CodeDto authCode,
                                @PathVariable String socialName) {

        Oauth2UserInfo userInfo = socialLoginService.getUserInfo(socialName, authCode.getCode());
        userInfo = oauth2UserService.putUserInto(userInfo);
        User user = userInfo.getUser();

        String accessToken =  jwtTokenProvider.responseAccessToken(user);
        String refreshToken = jwtTokenProvider.responseRefreshToken(user);
        redisUtil.setDataExpire(refreshToken, user.getSocialId(), JwtTokenProvider.REFRESH_TOKEN_USETIME / 1000L);

        return JwtResponseDto.builder()
                .accessToken(accessToken)
                .createdAt(user.getCreatedAt().toLocalDate().toString())
                .isFirstLogin(userInfo.isFirstLogin())
                .refreshToken(refreshToken)
                .statusCode(200)
                .responseMessage("Issuance completed Token")
                .build();
    }
}
