package com.sollertia.habit.domain.oauth2.loginutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.oauth2.dto.NaverOauthResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfoFactory;
import com.sollertia.habit.domain.user.ProviderType;
import com.sollertia.habit.exception.NaverOauth2Exception;
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
public class NaverSocialLoginUtil implements SocialLoginUtil {

    final static String NAVER_TOKEN_BASE_URL = "https://nid.naver.com/oauth2.0/token";
    final static String NAVER_TOKEN_INFO_URL = "https://openapi.naver.com/v1/nid/me";

    @Value("${oauth2.naver.client_id}")
    String clientId;
    @Value("${oauth2.naver.client_secret}")
    String clientSecret;

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) {
        throw new NaverOauth2Exception("네이버 로그인은 state 값이 필요합니다.");
    }

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode, String state) {
        try {
            String accessToken = getAccessTokenByCode(authCode, state);
            return getUserInfoByToken(accessToken);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private String getAccessTokenByCode(String authCode, String state) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        UriComponents builder = UriComponentsBuilder.fromHttpUrl(NAVER_TOKEN_BASE_URL)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("code", authCode)
                .queryParam("state", state)
                .queryParam("client_secret", clientSecret)
                .build();

        ResponseEntity<String> resultEntity = restTemplate.postForEntity(builder.toUriString(), null, String.class);
        NaverOauthResponseDto responseDto = mapper.readValue(resultEntity.getBody(), new TypeReference<NaverOauthResponseDto>(){});

        return responseDto.getAccess_token();
    }

    private Oauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
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
