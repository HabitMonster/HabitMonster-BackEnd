package com.sollertia.habit.domain.user.oauth2.userinfo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class NaverOauth2UserInfoTest {

    Map<String, Object> attributes = new HashMap<>();
    Map<String, Object> response = null;
    NaverOauth2UserInfo userInfo;

    @BeforeEach
    public void setUp() throws Exception {
        attributes.put("response", response);
        userInfo = new NaverOauth2UserInfo(attributes);
    }

    @Test
    public void getIdResponseIsNull() {
        //when, then
        assertThat(userInfo.getId()).isNull();
    }

    @Test
    public void getNameResponseIsNull() {
        //when, then
        assertThat(userInfo.getName()).isNull();
    }

    @Test
    public void getEmailResponseIsNull() {
        //when, then
        assertThat(userInfo.getEmail()).isNull();
    }

}