package com.sollertia.habit.domain.oauth2.loginutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.oauth2.userinfo.NaverOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.ProviderType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
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
@RestClientTest(NaverSocialLoginUtil.class)
class NaverSocialLoginUtilTest {
    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private NaverSocialLoginUtil naverSocialLoginUtil;

    String authCode;
    Map<String, Object> attributes = new HashMap<>();
    Map<String, Object> response = new HashMap<>();

    @BeforeEach
    public void setUp() throws Exception {
        authCode = "abcdefg1234567";
        response.put("id", "123456789");
        response.put("nickname", "tester");
        response.put("email", "tester.test.com");
        attributes.put("response", response);
        String responseJson =
                objectMapper.writeValueAsString(attributes);

        this.server.expect(requestTo(NaverSocialLoginUtil.NAVER_TOKEN_INFO_URL))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void getUserInfoByCode() {
        //given

        //when
        Oauth2UserInfo userInfo = naverSocialLoginUtil.getUserInfoByCode(authCode);

        //then
        assertThat(userInfo.getId()).isEqualTo(response.get("id")+"N");
        assertThat(userInfo.getName()).isEqualTo(response.get("nickname"));
        assertThat(userInfo.getEmail()).isEqualTo(response.get("email"));
        assertThat(userInfo.getProviderType()).isEqualTo(ProviderType.NAVER);
    }

}