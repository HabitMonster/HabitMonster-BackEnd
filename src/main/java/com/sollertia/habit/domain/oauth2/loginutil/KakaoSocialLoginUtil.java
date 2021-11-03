package com.sollertia.habit.domain.oauth2.loginutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.oauth2.dto.KakaoOauthResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfoFactory;
import com.sollertia.habit.domain.user.ProviderType;
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
public class KakaoSocialLoginUtil implements SocialLoginUtil {

    final static String KAKAO_TOKEN_BASE_URL = "https://kauth.kakao.com/oauth/token";
    final static String KAKAO_TOKEN_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("${oauth2.kakao.client_id}")
    String clientId;

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) {
        try {
            String accessToken = getAccessTokenByCode(authCode);
            return getUserInfoByToken(accessToken);
        } catch (JsonProcessingException exception) {
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private String getAccessTokenByCode(String authCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", "http://localhost:3000/login");
        body.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> kakaoOauthRequestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> resultEntity = restTemplate.postForEntity(KAKAO_TOKEN_BASE_URL, kakaoOauthRequestEntity, String.class);
        KakaoOauthResponseDto responseDto = mapper.readValue(resultEntity.getBody(), new TypeReference<KakaoOauthResponseDto>(){});

        return responseDto.getAccess_token();
    }

    private Oauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                KAKAO_TOKEN_INFO_URL,
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        Map<String,Object> userInfo = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>(){});
        return Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.KAKAO, userInfo);
    }
}
