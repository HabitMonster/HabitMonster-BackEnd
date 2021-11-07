package com.sollertia.habit.domain.user.oauth2;

import com.sollertia.habit.domain.user.oauth2.loginutil.GoogleSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.loginutil.KakaoSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.loginutil.NaverSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.service.SocialLoginService;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.KakaoOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.NaverOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.enums.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SocialLoginServiceTest {

    @InjectMocks
    private SocialLoginService socialLoginService;

    @Mock
    private GoogleSocialLoginUtil googleSocialLoginUtil;
    @Mock
    private KakaoSocialLoginUtil kakaoSocialLoginUtil;
    @Mock
    private NaverSocialLoginUtil naverSocialLoginUtil;

    String authCode;

    @BeforeEach
    private void before() {
        authCode = "abcdefg1234567";
    }

    @Test
    public void getGoogleUserInfo() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);

        //given
        given(googleSocialLoginUtil.getUserInfoByCode(authCode))
                .willReturn(oauth2UserInfo);

        //when
        Oauth2UserInfo googleUserInfo = socialLoginService.getUserInfo("google", authCode);

        //then
        assertThat(googleUserInfo.getId()).isEqualTo(attributes.get("sub")+"G");
        assertThat(googleUserInfo.getName()).isEqualTo(attributes.get("name"));
        assertThat(googleUserInfo.getEmail()).isEqualTo(attributes.get("email"));
        assertThat(googleUserInfo.getProviderType()).isEqualTo(ProviderType.GOOGLE);

        verify(googleSocialLoginUtil).getUserInfoByCode(authCode);
    }

    @Test
    public void getKakaoUserInfo() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", "tester");
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("id", "123456789");
        attributes.put("properties", properties);
        attributes.put("account_email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new KakaoOauth2UserInfo(attributes);

        //given
        given(kakaoSocialLoginUtil.getUserInfoByCode(authCode))
                .willReturn(oauth2UserInfo);

        //when
        Oauth2UserInfo kakaoUserInfo = socialLoginService.getUserInfo("kakao", authCode);

        //then
        assertThat(kakaoUserInfo.getId()).isEqualTo(attributes.get("id")+"K");
        assertThat(kakaoUserInfo.getName()).isEqualTo(properties.get("nickname"));
        assertThat(kakaoUserInfo.getEmail()).isEqualTo(attributes.get("account_email"));
        assertThat(kakaoUserInfo.getProviderType()).isEqualTo(ProviderType.KAKAO);

        verify(kakaoSocialLoginUtil).getUserInfoByCode(authCode);
    }

    @Test
    public void getNaverUserInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("id", "123456789");
        response.put("nickname", "tester");
        response.put("email", "tester.test.com");
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("response", response);
        Oauth2UserInfo oauth2UserInfo = new NaverOauth2UserInfo(attributes);

        //given
        given(naverSocialLoginUtil.getUserInfoByCode(authCode))
                .willReturn(oauth2UserInfo);

        //when
        Oauth2UserInfo naverUserInfo = socialLoginService.getUserInfo("naver", authCode);

        //then
        assertThat(naverUserInfo.getId()).isEqualTo(response.get("id")+"N");
        assertThat(naverUserInfo.getName()).isEqualTo(response.get("nickname"));
        assertThat(naverUserInfo.getEmail()).isEqualTo(response.get("email"));
        assertThat(naverUserInfo.getProviderType()).isEqualTo(ProviderType.NAVER);

        verify(naverSocialLoginUtil).getUserInfoByCode(authCode);
    }

}