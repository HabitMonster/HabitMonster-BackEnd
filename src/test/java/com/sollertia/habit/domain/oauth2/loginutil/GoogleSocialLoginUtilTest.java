package com.sollertia.habit.domain.oauth2.loginutil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GoogleSocialLoginUtilTest {

    @InjectMocks
    private GoogleSocialLoginUtil googleSocialLoginUtil;

    @Mock
    private RestTemplate restTemplate;

    String authCode;

    @BeforeEach
    private void before() {
        authCode = "abcdefg1234567";
    }

    @Test
    void getUserInfoByCode() {
        //given

        //when
        googleSocialLoginUtil.getUserInfoByCode(authCode);

        //then
    }
}