package com.sollertia.habit.domain.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
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
    private final Oauth2UserService oAuth2UserService;

    //Authentication Code를 전달 받는 엔드포인트
    @GetMapping("/oauth/{providerType}/token")
    public ResponseEntity<Oauth2UserInfo> auth(@RequestParam(value = "code") String authCode, @PathVariable String providerType)
            throws JsonProcessingException {

        GoogleOauth2UserInfo userInfo = googleOauth2Service.getUserInfoByCode(authCode);
        User user = oAuth2UserService.loadUser(userInfo);


        //todo 서버 access token, refresh token 생성 하고 전달
        return ResponseEntity.ok().body(userInfo);
    }
}
