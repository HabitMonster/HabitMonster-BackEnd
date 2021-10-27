package com.sollertia.habit.domain.oauth2.userinfo;

import com.sollertia.habit.domain.user.ProviderType;

import java.util.Map;

public class GoogleOauth2UserInfo extends Oauth2UserInfo {

    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.GOOGLE;
    }
}