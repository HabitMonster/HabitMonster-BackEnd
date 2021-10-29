package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.config.jwt.JwtHandler;
import com.sollertia.habit.config.jwt.JwtTokenProvider;
import com.sollertia.habit.config.jwt.dto.JwtResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.utils.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {

    private final Oauth2UserService oauth2UserService;
    private final SocialLoginService socialLoginService;
    private final JwtHandler jwtHandler;
    private final RedisUtil redisUtil;

    @GetMapping("/user/login/{socialName}")
    public ResponseEntity<JwtResponseDto> login(@RequestParam(value = "code") String authCode,
                                                @Nullable @RequestParam(value = "state") String state,
                                                @PathVariable String socialName) {

        Oauth2UserInfo userInfo = socialLoginService.getUserInfo(socialName, authCode, state);
        userInfo = oauth2UserService.putUserInto(userInfo);

        boolean isFirstLogin = userInfo.isFirstLogin();
        User user = userInfo.getUser();

        //todo 서버 access token, refresh token 생성 하고 전달
        String accessToken = jwtHandler.getAccessToken(user);
        String refreshToken = jwtHandler.getRefreshToken(user);
        redisUtil.setDataExpire(refreshToken, user.getUserId(), JwtTokenProvider.REFRESH_TOKEN_USETIME / 1000L);

        return ResponseEntity.ok().body(JwtResponseDto.builder().accesstoken(accessToken).
                isFirstLongin(isFirstLogin).refreshtoken(refreshToken).build());
    }
}
