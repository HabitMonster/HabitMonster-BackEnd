package com.sollertia.habit.domain.user.oauth2.userinfo;

import com.sollertia.habit.domain.user.enums.ProviderType;

import java.util.Map;

public class GoogleOauth2UserInfo extends Oauth2UserInfo {

    public GoogleOauth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return attributes.get("sub")+"G";
    }


    @Override
    public String getName() {
        String name = (String) attributes.get("name");
        name = name.replace(" ", "");
        return name;
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