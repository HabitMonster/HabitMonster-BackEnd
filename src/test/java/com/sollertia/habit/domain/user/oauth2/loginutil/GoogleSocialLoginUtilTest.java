package com.sollertia.habit.domain.user.oauth2.loginutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.user.oauth2.loginutil.GoogleSocialLoginUtil;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.enums.ProviderType;
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
@RestClientTest(GoogleSocialLoginUtil.class)
class GoogleSocialLoginUtilTest {

    @Autowired
    private MockRestServiceServer server;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private GoogleSocialLoginUtil googleSocialLoginUtil;

    String authCode;
    Map<String, Object> attributes = new HashMap<>();

    @BeforeEach
    public void setUp() throws Exception {
        authCode = "abcdefg1234567";
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        String responseJson =
                objectMapper.writeValueAsString(attributes);

        this.server.expect(requestTo(GoogleSocialLoginUtil.GOOGLE_TOKEN_INFO_URL
                        +"?id_token="+authCode))
                .andRespond(withSuccess(responseJson, MediaType.APPLICATION_JSON));
    }

    @Test
    void getUserInfoByCode() {
        //given

        //when
        Oauth2UserInfo userInfo = googleSocialLoginUtil.getUserInfoByCode(authCode);

        //then
        assertThat(userInfo.getId()).isEqualTo(attributes.get("sub")+"G");
        assertThat(userInfo.getName()).isEqualTo(attributes.get("name"));
        assertThat(userInfo.getEmail()).isEqualTo(attributes.get("email"));
        assertThat(userInfo.getProviderType()).isEqualTo(ProviderType.GOOGLE);
    }
}