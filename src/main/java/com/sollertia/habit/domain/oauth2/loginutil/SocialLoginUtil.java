package com.sollertia.habit.domain.oauth2.loginutil;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;

public interface SocialLoginUtil {

    Oauth2UserInfo getUserInfoByCode(String authCode);
}
