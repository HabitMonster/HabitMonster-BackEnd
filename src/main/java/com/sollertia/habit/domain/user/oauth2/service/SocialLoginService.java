package com.sollertia.habit.domain.user.oauth2.service;

import com.sollertia.habit.domain.user.oauth2.loginutil.GoogleSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.loginutil.KakaoSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.loginutil.NaverSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.loginutil.SocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.enums.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SocialLoginService {

    Map<ProviderType, SocialLoginUtil> loginUtilMap = new HashMap<>();

    @Autowired
    private SocialLoginService(GoogleSocialLoginUtil googleSocialLoginUtil,
                               NaverSocialLoginUtil naverSocialLoginUtil,
                               KakaoSocialLoginUtil kakaoSocialLoginUtil) {
        loginUtilMap.put(ProviderType.GOOGLE, googleSocialLoginUtil);
        loginUtilMap.put(ProviderType.KAKAO, kakaoSocialLoginUtil);
        loginUtilMap.put(ProviderType.NAVER, naverSocialLoginUtil);
    }

    public Oauth2UserInfo getUserInfo(String socialName, String authCode) {
        ProviderType providerType = ProviderType.valueOf(socialName.toUpperCase());
        return loginUtilMap.get(providerType).getUserInfoByCode(authCode);
    }
}
