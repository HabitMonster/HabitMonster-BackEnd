package com.sollertia.habit.domain.user.oauth2.service;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.KakaoOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.OAuthProviderMissMatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class Oauth2UserServiceTest {

    @InjectMocks
    private Oauth2UserService oauth2UserService;

    @Mock
    private UserRepository userRepository;

    private User googleUser;
    private User kakaoUser;
    private Oauth2UserInfo googleUserInfo;
    private Oauth2UserInfo kakaoUserInfo;

    @BeforeEach
    void setUp() {
        //given
        Map<String, Object> googleAttributes = new HashMap<>();
        googleAttributes.put("sub", "123456789");
        googleAttributes.put("name", "tester");
        googleAttributes.put("email", "tester.test.com");
        Map<String, Object> kakaoAttributes = new HashMap<>();
        Map<String, Object> properties = new HashMap<>();
        properties.put("nickname", "tester");
        kakaoAttributes.put("id", "123456789");
        kakaoAttributes.put("properties", properties);
        kakaoAttributes.put("account_email", "tester.test.com");
        googleUserInfo = new GoogleOauth2UserInfo(googleAttributes);
        googleUser = User.create(googleUserInfo);

        kakaoUserInfo = new KakaoOauth2UserInfo(kakaoAttributes);
        kakaoUser = User.create(kakaoUserInfo);
    }

    @Test
    void putUserInto() {
        //given
        given(userRepository.findBySocialId(googleUser.getSocialId()))
                .willReturn(Optional.of(googleUser));

        //when
        Oauth2UserInfo oauth2UserInfo = oauth2UserService.putUserInto(googleUserInfo);

        //then
        assertThat(oauth2UserInfo.getUser()).isEqualTo(googleUser);
        assertThat(oauth2UserInfo.getEmail()).isEqualTo(googleUserInfo.getEmail());
        assertThat(oauth2UserInfo.getName()).isEqualTo(googleUserInfo.getName());
        assertThat(oauth2UserInfo.getId()).isEqualTo(googleUserInfo.getId());
        assertThat(oauth2UserInfo.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(oauth2UserInfo.isFirstLogin()).isTrue();

        verify(userRepository).findBySocialId(googleUser.getSocialId());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void putNewUserInto() {
        //given
        given(userRepository.findBySocialId(googleUser.getSocialId()))
                .willReturn(Optional.empty());
        given(userRepository.save(any(User.class)))
                .willReturn(googleUser);

        //when
        Oauth2UserInfo oauth2UserInfo = oauth2UserService.putUserInto(googleUserInfo);

        //then
        assertThat(oauth2UserInfo.getUser()).isEqualTo(googleUser);
        assertThat(oauth2UserInfo.getEmail()).isEqualTo(googleUserInfo.getEmail());
        assertThat(oauth2UserInfo.getName()).isEqualTo(googleUserInfo.getName());
        assertThat(oauth2UserInfo.getId()).isEqualTo(googleUserInfo.getId());
        assertThat(oauth2UserInfo.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(oauth2UserInfo.isFirstLogin()).isTrue();

        verify(userRepository).findBySocialId(googleUser.getSocialId());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void putUserProviderMissMatch() {
        //given
        given(userRepository.findBySocialId(googleUser.getSocialId()))
                .willReturn(Optional.of(kakaoUser));

        //when, then
        assertThrows(OAuthProviderMissMatchException.class,
                () -> oauth2UserService.putUserInto(googleUserInfo));

        verify(userRepository).findBySocialId(googleUser.getSocialId());
        verify(userRepository, never()).save(any(User.class));
    }
}