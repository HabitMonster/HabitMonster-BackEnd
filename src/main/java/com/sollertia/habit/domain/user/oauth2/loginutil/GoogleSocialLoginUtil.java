package com.sollertia.habit.domain.user.oauth2.loginutil;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.sollertia.habit.domain.user.enums.ProviderType;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class GoogleSocialLoginUtil implements SocialLoginUtil {

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleSocialLoginUtil(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
    final static String GOOGLE_TOKEN_INFO_URL = "https://oauth2.googleapis.com/tokeninfo";

    @Override
    public Oauth2UserInfo getUserInfoByCode(String authCode) {
        try {
            return getUserInfoByToken(authCode);
        } catch (JsonProcessingException exception) {
            throw new JsonParseException(exception);
        }
    }

    private Oauth2UserInfo getUserInfoByToken(String token) throws JsonProcessingException {
        ObjectMapper mapper = getObjectMapperInstance();

        String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_TOKEN_INFO_URL)
                .queryParam("id_token", token).encode().toUriString();

        String resultJson = restTemplate.getForObject(requestUrl, String.class);
        Map<String,Object> userInfo = mapper.readValue(resultJson, new TypeReference<Map<String, Object>>(){});

        return Oauth2UserInfoFactory.getOAuth2UserInfo(ProviderType.GOOGLE, userInfo);
    }

    private ObjectMapper getObjectMapperInstance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
