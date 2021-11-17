package com.sollertia.habit.domain.user.oauth2.loginutil;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.user.oauth2.dto.KakaoOauthResponseDto;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfoFactory;
import com.sollertia.habit.domain.user.enums.ProviderType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class KakaoSocialLoginUtil implements SocialLoginUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public KakaoSocialLoginUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    final static String KAKAO_TOKEN_BASE_URL = "https://kauth.kakao.com/oauth/token";
    final static String KAKAO_TOKEN_INFO_URL = "https://kapi.kakao.com/v2/user/me";

    @Value("${oauth2.kakao.client_id}")
    String clientId;
    @Value("${oauth2.kakao.redirect_url}")
    String redirectUrl;

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
        ObjectMapper mapper = new ObjectMapper();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUrl);
        body.add("code", authCode);

        HttpEntity<MultiValueMap<String, String>> kakaoOauthRequestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> resultEntity = restTemplate.postForEntity(KAKAO_TOKEN_BASE_URL, kakaoOauthRequestEntity, String.class);
        KakaoOauthResponseDto responseDto = mapper.readValue(resultEntity.getBody(), new TypeReference<KakaoOauthResponseDto>(){});

        return responseDto.getAccess_token();
    }

    private Oauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
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
