package com.sollertia.habit.domain.user.oauth2.userinfo;

import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.global.exception.user.InvalidSocialNameException;

import java.util.Map;

public class Oauth2UserInfoFactory {
    public static Oauth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOauth2UserInfo(attributes);
            case NAVER: return new NaverOauth2UserInfo(attributes);
            case KAKAO: return new KakaoOauth2UserInfo(attributes);
            default: throw new InvalidSocialNameException("잘못된 소셜 로그인 타입입니다.");
        }
    }

    private Oauth2UserInfoFactory() {
    }
}