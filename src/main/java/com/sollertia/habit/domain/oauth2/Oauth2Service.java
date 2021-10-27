package com.sollertia.habit.domain.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;

public interface Oauth2Service {
    Oauth2UserInfo getUserInfoByCode(String authCode) throws JsonProcessingException;
}
