package com.sollertia.habit.domain.oauth2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.oauth2.dto.KakaoOauthResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.KakaoOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class KakaoOauth2Service implements Oauth2Service {

    final static String KAKAO_TOKEN_BASE_URL = "https://kauth.kakao.com/oauth/token";

    @Value("${oauth2.kakao.client_id}")
    String clientId;

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) throws JsonProcessingException {
        String accessToken = getAccessTokenByCode(authCode);
        return getUserInfoByToken(accessToken);
    }

    private String getAccessTokenByCode(String authCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", "http://localhost:8080/user/login/test/kakao");
        body.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> kakaoOauthRequestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> resultEntity = restTemplate.postForEntity(KAKAO_TOKEN_BASE_URL, kakaoOauthRequestEntity, String.class);
        KakaoOauthResponseDto responseDto = mapper.readValue(resultEntity.getBody(), new TypeReference<KakaoOauthResponseDto>(){});

        return responseDto.getAccess_token();
    }

    private KakaoOauth2UserInfo getUserInfoByToken(String accessToken) throws JsonProcessingException {
        // 2. 토큰으로 카카오 API 호출
        // HTTP Header 생성
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        Map<String,Object> userInfo = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
        return new KakaoOauth2UserInfo(userInfo);
    }
}
