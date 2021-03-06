package com.sollertia.habit.domain.user.oauth2.loginutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class NaverSocialLoginUtil implements SocialLoginUtil {

    protected final RestTemplate restTemplate;

    @Autowired
    public NaverSocialLoginUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    final static String NAVER_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    final static String NAVER_TOKEN_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) {
        try {
            return getUserInfoByToken(authCode);
        } catch (JsonProcessingException exception) {
            throw new JsonParseException(exception);
        }
    }

    private Oauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        HttpEntity<Map<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_TOKEN_INFO_URL,
                HttpMethod.GET,
                naverUserInfoRequest,
                String.class
        );

        Map<String,Object> userInfo = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
        return Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.NAVER, userInfo);
    }
}
