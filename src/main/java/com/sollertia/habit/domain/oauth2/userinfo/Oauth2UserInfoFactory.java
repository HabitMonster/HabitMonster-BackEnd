package com.sollertia.habit.domain.oauth2.userinfo;

import com.sollertia.habit.domain.user.ProviderType;

import java.util.Map;

public class Oauth2UserInfoFactory {
    public static Oauth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOauth2UserInfo(attributes);
//            case NAVER: return new NaverOAuth2UserInfo(attributes);
//            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}