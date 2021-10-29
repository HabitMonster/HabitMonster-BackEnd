package com.sollertia.habit.domain.oauth2.userinfo;

import com.sollertia.habit.domain.user.ProviderType;
import com.sollertia.habit.domain.user.User;

import java.util.Map;

public abstract class Oauth2UserInfo {
    protected Map<String, Object> attributes;

    protected User user;

    protected boolean isFirstLogin = false;

    public Oauth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract ProviderType getProviderType();

    public User getUser() {
        return user;
    }

    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public void putUser(User user) {
        this.setUser(user);
    }

    public void toFirstLogin() {
        this.setFirstLogin(true);
    }
}