package com.sollertia.habit.domain.user.oauth2.userinfo;

import com.sollertia.habit.domain.user.enums.ProviderType;

import java.util.Map;

public class NaverOauth2UserInfo extends Oauth2UserInfo {

    public NaverOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return response.get("id")+"N";
    }

    @Override
    public String getName() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        if (response == null) {
            return null;
        }

        return (String) response.get("email");
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.NAVER;
    }
}
