package com.sollertia.habit.domain.user.security.jwt.filter;

import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    MockHttpServletRequest request = new MockHttpServletRequest();
    User testUser;
    private String secretKey = "test";
    private String token;
    public static final String signatureToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0RyIsInR5cGUiOiJHb29nbGUiLCJpYXQiOjE2MzYwMjQ4NzcsImV4cCI6MTYzNjAyNDg4Mn0.OAXTkojY2evdc-NsU2Za0Bv6VaWsv0FQVlhSt7QhxbY";

    public static final String expiredToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxNjM2MDM1ODI3fQ.zI4KZQPw_L8yEH4XdsKYMF5ZDaY5k-IzFYkWA9wEgas";

    private static final String successToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxODg2MzYyODY2fQ.qHqotXvz0vipsiQmHiS3utO-43yop98fdUK9XcMXXWQ";
    @BeforeEach
    private void beforeEach() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        request.addHeader("A-AUTH-TOKEN", "abcdefg");
        request.addHeader("R-AUTH-TOKEN", "hijklmn");
    }

    @DisplayName("secretKey Base64 ????????? ??????")
    @Test
    void init() {
        //then
        assertThat(secretKey).isBase64();
    }

    @DisplayName("RefreshToken ??????")
    @Test
    void responseRefreshToken() {
        //then
        assertThat(jwtTokenProvider.responseRefreshToken(testUser))
                .isInstanceOf(String.class);
    }

    @DisplayName("AccessToken ??????")
    @Test
    void responseAccessToken() {
        //then
        assertThat(jwtTokenProvider.responseAccessToken(testUser))
                .isInstanceOf(String.class);
    }

    @DisplayName("Token Authentication ?????? ??? ??????")
    @Test
    void getAuthentication() {
        //given
        given(userDetailsService.loadUserByUsername(testUser.getSocialId())).willReturn(new UserDetailsImpl(testUser));
        token = jwtTokenProvider.responseAccessToken(testUser);

        assertThat(jwtTokenProvider.getAuthentication(token)).isInstanceOf(Authentication.class);
    }

    @DisplayName("Token claim ?????? ??????")
    @Test
    void getSocialId() {
        //when
        token = jwtTokenProvider.responseAccessToken(testUser);
        //then
        assertThat(jwtTokenProvider.getSocialId(token)).isEqualTo(testUser.getSocialId());
    }

    @DisplayName("Header AccessToken ??????")
    @Test
    void requestAccessToken() {
        //then
        assertThat(jwtTokenProvider.requestAccessToken(request)).isEqualTo("abcdefg");
    }

    @DisplayName("Header RefreshToken ??????")
    @Test
    void requestRefreshToken() {
        //then
        assertThat(jwtTokenProvider.requestRefreshToken(request)).isEqualTo("hijklmn");
    }

    @DisplayName("Token ????????? SignatureException ?????? ??????")
    @Test
    void validateTokenSignature() {
        assertThrows(SignatureException.class, () -> jwtTokenProvider.validateToken(signatureToken));
    }

    @DisplayName("Token ????????? MalformedJwtException ?????? ??????")
    @Test
    void validateTokenMalformed() {
        assertThrows(MalformedJwtException.class, () -> jwtTokenProvider.getAuthentication("abcd"));
    }

    @DisplayName("Token ????????? ExpiredJwtException ?????? ??????")
    @Test
    void validateTokenExpiredJwt() {
        assertThrows(ExpiredJwtException.class, () -> jwtTokenProvider.validateToken(expiredToken));
    }

    @DisplayName("SuccessToken ????????? ??????")
    @Test
    void accessTokenNotNull(){
        jwtTokenProvider.validateToken(successToken);
    }

}