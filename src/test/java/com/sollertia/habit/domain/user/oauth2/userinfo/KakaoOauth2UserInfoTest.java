package com.sollertia.habit.domain.user.oauth2.userinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class KakaoOauth2UserInfoTest {

    Map<String, Object> attributes = new HashMap<>();
    Map<String, Object> properties = null;
    KakaoOauth2UserInfo userInfo;

    @BeforeEach
    public void setUp() throws Exception {
        attributes.put("id", "123456789");
        attributes.put("properties", properties);
        attributes.put("account_email", "tester.test.com");
        userInfo = new KakaoOauth2UserInfo(attributes);
    }

    @Test
    void getNamePropertiesIsNull() {
        assertThat(userInfo.getName()).isNull();
    }
}