package com.sollertia.habit.domain.user.oauth2.userinfo;

import com.sollertia.habit.domain.user.enums.ProviderType;

import java.util.Map;

public class KakaoOauth2UserInfo extends Oauth2UserInfo {

    public KakaoOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("id") + "K";
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");

        if (properties == null) {
            return null;
        }

        String name = (String) properties.get("nickname");
        name = name.replace(" ", "");
        return name;
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("account_email");
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.KAKAO;
    }
}
