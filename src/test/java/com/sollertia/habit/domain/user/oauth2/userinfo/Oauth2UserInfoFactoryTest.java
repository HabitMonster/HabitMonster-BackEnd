package com.sollertia.habit.domain.user.oauth2.userinfo;

import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.global.exception.user.InvalidSocialNameException;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Oauth2UserInfoFactoryTest {

    Map<String, Object> attributes;

    @Test
    void getGoogleUserInfo() {
        //when
        Oauth2UserInfo userInfo = Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.GOOGLE, attributes);

        //then
        assertTrue(userInfo instanceof GoogleOauth2UserInfo);
    }

    @Test
    void getNaverUserInfo() {
        //when
        Oauth2UserInfo userInfo = Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.NAVER, attributes);

        //then
        assertTrue(userInfo instanceof NaverOauth2UserInfo);
    }

    @Test
    void getKakaoUserInfo() {
        //when
        Oauth2UserInfo userInfo = Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.KAKAO, attributes);

        //then
        assertTrue(userInfo instanceof KakaoOauth2UserInfo);
    }

    @Test
    void getOauth2UserInfoInvalidType() {
        //when, then
        assertThrows(InvalidSocialNameException.class,
                () -> Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.NONE, attributes));
    }
}