package com.sollertia.habit.domain.user.oauth2.loginutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.user.oauth2.dto.KakaoOauthResponseDto;
import com.sollertia.habit.domain.user.oauth2.loginutil.KakaoSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.enums.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ExtendWith(MockitoExtension.class)
@RestClientTest(KakaoSocialLoginUtil.class)
@RunWith(PowerMockRunner.class)
class KakaoSocialLoginUtilTest {

    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private KakaoSocialLoginUtil kakaoSocialLoginUtil;

    String authCode;
    Map<String, Object> attributes = new HashMap<>();
    Map<String, Object> properties = new HashMap<>();

    @BeforeEach
    public void setUp() throws Exception {
        authCode = "abcdefg1234567";
        KakaoOauthResponseDto responseDto = new KakaoOauthResponseDto();
        Whitebox.setInternalState(responseDto, "token_type", "token_type");
        Whitebox.setInternalState(responseDto, "access_token", "access_token");
        Whitebox.setInternalState(responseDto, "expires_in", "expires_in");
        Whitebox.setInternalState(responseDto, "refresh_token", "refresh_token");
        Whitebox.setInternalState(responseDto, "refresh_token_expires_in", "refresh_token_expires_in");
        Whitebox.setInternalState(responseDto, "scope", "scope");

        String responseTokenJson = objectMapper.writeValueAsString(responseDto);
        properties.put("nickname", "tester");
        attributes.put("id", "123456789");
        attributes.put("properties", properties);
        attributes.put("account_email", "tester.test.com");
        String responseJson =
                objectMapper.writeValueAsString(attributes);

        this.server.expect(requestTo(KakaoSocialLoginUtil.KAKAO_TOKEN_BASE_URL))
                .andRespond(withSuccess(responseTokenJson, MediaType.APPLICATION_JSON));
        this.server.expect(requestTo(KakaoSocialLoginUtil.KAKAO_TOKEN_INFO_URL))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void getUserInfoByCode() {
        //given

        //when
        Oauth2UserInfo userInfo = kakaoSocialLoginUtil.getUserInfoByCode(authCode);

        //then
        assertThat(userInfo.getId()).isEqualTo(attributes.get("id")+"K");
        assertThat(userInfo.getName()).isEqualTo(properties.get("nickname"));
        assertThat(userInfo.getEmail()).isEqualTo(attributes.get("account_email"));
        assertThat(userInfo.getProviderType()).isEqualTo(ProviderType.KAKAO);
    }
}