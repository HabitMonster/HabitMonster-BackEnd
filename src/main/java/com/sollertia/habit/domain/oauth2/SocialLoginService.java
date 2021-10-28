package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SocialLoginService {

    Map<ProviderType, SocialLoginUtil> loginUtilMap = new HashMap<>();

    @Autowired
    public SocialLoginService(GoogleSocialLoginUtil googleOauth2Service, NaverSocialLoginUtil naverOauth2Service, KakaoSocialLoginUtil kakaoOauth2Service) {
        loginUtilMap.put(ProviderType.GOOGLE, googleOauth2Service);
        loginUtilMap.put(ProviderType.KAKAO, kakaoOauth2Service);
        loginUtilMap.put(ProviderType.NAVER, naverOauth2Service);
    }

    Oauth2UserInfo getUserInfo(String socialName, String authCode, String state) {
        ProviderType providerType = ProviderType.valueOf(socialName.toUpperCase());
        return loginUtilMap.get(providerType).getUserInfoByCode(authCode, state);
    }
}
