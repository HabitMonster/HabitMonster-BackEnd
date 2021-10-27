package com.sollertia.habit.domain.oauth2;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.sollertia.habit.domain.oauth2.dto.GoogleOauthRequestDto;
import com.sollertia.habit.domain.oauth2.dto.GoogleOauthResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class GoogleOauth2Service implements Oauth2Service {

    final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    final static String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    @Value("${api.client_id}")
    String clientId;
    @Value("${api.client_secret}")
    String clientSecret;

    @Override
    public GoogleOauth2UserInfo getUserInfoByCode(String authCode) throws JsonProcessingException {
        String accessToken = getAccessTokenByCode(authCode);
        return getUserInfoByToken(accessToken);
    }

    private String getAccessTokenByCode(String authCode) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = getObjectMapperInstance();

        GoogleOauthRequestDto requestDto = GoogleOauthRequestDto
                .builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .code(authCode)
                .redirectUri("http://localhost:8080/oauth/google")
                .grantType("authorization_code")
                .build();

        ResponseEntity<String> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, requestDto, String.class);
        GoogleOauthResponseDto responseDto = mapper.readValue(resultEntity.getBody(), new TypeReference<GoogleOauthResponseDto>(){});

        return responseDto.getIdToken();
    }

    private GoogleOauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper mapper = getObjectMapperInstance();

        String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_INFO_URL)
                .queryParam("id_token", token).encode().toUriString();

        String resultJson = restTemplate.getForObject(requestUrl, String.class);
        Map<String,Object> userInfo = mapper.readValue(resultJson, new TypeReference<Map<String, Object>>(){});

        return new GoogleOauth2UserInfo(userInfo);
    }

    private ObjectMapper getObjectMapperInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
