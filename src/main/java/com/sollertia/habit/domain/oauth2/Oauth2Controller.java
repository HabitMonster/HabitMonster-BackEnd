package com.sollertia.habit.domain.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.ProviderType;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.exception.InvalidSocialNameException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Oauth2Controller {

    private final GoogleOauth2Service googleOauth2Service;
    private final KakaoOauth2Service kakaoOauth2Service;
    private final Oauth2UserService oauth2UserService;

    //Authentication Code를 전달 받는 엔드포인트
    @GetMapping("/user/login/{socialName}")
    public ResponseEntity<Oauth2UserInfo> login(@RequestParam(value = "code") String authCode, @PathVariable String socialName)
            throws JsonProcessingException {
        ProviderType providerType = ProviderType.valueOf(socialName.toUpperCase());
        Oauth2UserInfo userInfo;
        switch (providerType) {
            case GOOGLE: userInfo = googleOauth2Service.getUserInfoByCode(authCode); break;
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: userInfo = kakaoOauth2Service.getUserInfoByCode(authCode); break;
            default: throw new InvalidSocialNameException("올바른 소셜 로그인 서비스 이름이 아닙니다.");
        }
        boolean isFirstLogin = oauth2UserService.isFirstLogin(userInfo);
        User user = oauth2UserService.loadUser(userInfo, isFirstLogin);


        //todo 서버 access token, refresh token 생성 하고 전달
        return ResponseEntity.ok().body(userInfo);
    }
}
