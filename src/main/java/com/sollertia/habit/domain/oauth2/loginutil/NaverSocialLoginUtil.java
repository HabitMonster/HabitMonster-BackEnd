package com.sollertia.habit.domain.oauth2.loginutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.oauth2.dto.NaverOauthResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfoFactory;
import com.sollertia.habit.domain.user.ProviderType;
import com.sollertia.habit.exception.NaverOauth2Exception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverSocialLoginUtil implements SocialLoginUtil {

    private final RestTemplate restTemplate;

    final static String NAVER_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    final static String NAVER_TOKEN_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    @Value("${oauth2.naver.client_id}")
    String clientId;
    @Value("${oauth2.naver.client_secret}")
    String clientSecret;

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) {
        try {
            return getUserInfoByToken(authCode);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            return null;
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
