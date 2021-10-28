package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;

public interface SocialLoginUtil {

    Oauth2UserInfo getUserInfoByCode(String authCode);
    Oauth2UserInfo getUserInfoByCode(String authCode, String state);
}
