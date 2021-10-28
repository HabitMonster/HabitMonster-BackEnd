package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
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

    @GetMapping("/user/login/{socialName}")
    public ResponseEntity<Oauth2UserInfo> login(@RequestParam(value = "code") String authCode,
                                                @Nullable @RequestParam(value = "state") String state,
                                                @PathVariable String socialName) {

        Oauth2UserInfo userInfo = socialLoginService.getUserInfo(socialName, authCode, state);
        userInfo = oauth2UserService.putUserInto(userInfo);

        boolean isFirstLogin = userInfo.isFirstLogin();
        User User = userInfo.getUser();

        //todo 서버 access token, refresh token 생성 하고 전달
        return ResponseEntity.ok().body(userInfo);
    }
}
