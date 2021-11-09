package com.sollertia.habit.domain.user.security.jwt.filter;

import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtAuthenticationFilter;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.global.utils.RedisUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;


@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RedisUtil redisUtil;

    Authentication authentication;
    MockHttpServletResponse response = new MockHttpServletResponse();
    MockHttpServletRequest request = new MockHttpServletRequest();
    MockFilterChain chain = new MockFilterChain();
    User testUser;

    public static final String signatureToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0RyIsInR5cGUiOiJHb29nbGUiLCJpYXQiOjE2MzYwMjQ4NzcsImV4cCI6MTYzNjAyNDg4Mn0.OAXTkojY2evdc-NsU2Za0Bv6VaWsv0FQVlhSt7QhxbY";

    public static final String expiredToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxNjM2MDM1ODI3fQ.zI4KZQPw_L8yEH4XdsKYMF5ZDaY5k-IzFYkWA9wEgas";

    private static final String successToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxODg2MzYyODY2fQ.qHqotXvz0vipsiQmHiS3utO-43yop98fdUK9XcMXXWQ";

    private static final String MalformedToken = "abcdefg";

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
    }

    @DisplayName("accessToken이 notNull 이면서 토큰에 문제가 없는 경우")
    @Test
    void accessTokenNotNull() throws ServletException, IOException {
        //given
        given(jwtTokenProvider.requestAccessToken(request)).willReturn(successToken);
        given(jwtTokenProvider.getAuthentication(successToken)).willReturn(authentication);
        //when - then
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
    }

    @DisplayName("accessToken이 notNull 이면서 ExpiredJwtException")
    @Test
    void accessTokenNotNullExpiredException() {
        //given
        given(jwtTokenProvider.requestAccessToken(request)).willReturn(expiredToken);
        willThrow(ExpiredJwtException.class).given(jwtTokenProvider).validateToken(expiredToken);
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(ExpiredJwtException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));

    }

    @DisplayName("accessToken이 notNull 이면서 SignatureException")
    @Test
    void accessTokenNotNullSignatureException() {
        //given
        given(jwtTokenProvider.requestAccessToken(request)).willReturn(signatureToken);
        willThrow(SignatureException.class).given(jwtTokenProvider).validateToken(signatureToken);
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(SignatureException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));

    }

    @DisplayName("accessToken이 notNull 이면서 MalformedJwtException")
    @Test
    void accessTokenNotNullMalformedJwtException() {
        //given
        given(jwtTokenProvider.requestAccessToken(request)).willReturn(MalformedToken);
        willThrow(MalformedJwtException.class).given(jwtTokenProvider).getAuthentication(MalformedToken);
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(MalformedJwtException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));

    }

    @DisplayName("refreshToken이 notNull 이면서 토큰에 문제가 없는 경우")
    @Test
    void refreshTokenNotNull() throws ServletException, IOException {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(successToken);
        given(jwtTokenProvider.getAuthentication(successToken)).willReturn(authentication);
        given(redisUtil.getData(successToken)).willReturn(testUser.getSocialId());
        given(jwtTokenProvider.getSocialId(successToken)).willReturn(testUser.getSocialId());
        //when - then
        jwtAuthenticationFilter.doFilterInternal(request, response, chain);
    }

    @DisplayName("refreshToken이 notNull redisId Miss Matching")
    @Test
    void refreshTokenNotNullRedisMissmatch() {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(successToken);
        given(jwtTokenProvider.getSocialId(successToken)).willReturn(testUser.getSocialId());
        given(redisUtil.getData(successToken)).willReturn("abc");
        request.setRequestURI("/test");
        request.setMethod("GET");
        //when - then
        assertThrows(JwtException.class, () ->
                jwtAuthenticationFilter.doFilterInternal(request, response, chain));
    }

    @DisplayName("refreshToken이 notNull redisUtilException")
    @Test
    void refreshTokenNotNullredisUtilException() {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(expiredToken);
        given(redisUtil.getData(successToken)).willReturn(testUser.getSocialId());
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(Exception.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));
    }

    @DisplayName("refreshToken이 notNull ExpiredJwtException")
    @Test
    void refreshTokenNotNullExpired() {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(expiredToken);
        given(jwtTokenProvider.getSocialId(expiredToken)).willReturn(testUser.getSocialId());
        given(redisUtil.getData(expiredToken)).willReturn(testUser.getSocialId());
        request.setRequestURI("/test");
        request.setMethod("GET");
        willThrow(ExpiredJwtException.class).given(jwtTokenProvider).validateToken(expiredToken);
        assertThrows(ExpiredJwtException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));
    }

    @DisplayName("refreshToken이 notNull SignatureException")
    @Test
    void refreshTokenNotNullSignature() {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(signatureToken);
        given(redisUtil.getData(signatureToken)).willReturn(testUser.getSocialId());
        given(jwtTokenProvider.getSocialId(signatureToken)).willReturn(testUser.getSocialId());
        willThrow(SignatureException.class).given(jwtTokenProvider).validateToken(signatureToken);
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(SignatureException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));
    }

    @DisplayName("refreshToken이 notNull MalformedJwtException")
    @Test
    void refreshTokenNotNullMalformed() {
        //given
        given(jwtTokenProvider.requestRefreshToken(request)).willReturn(MalformedToken);
        given(redisUtil.getData(MalformedToken)).willReturn(testUser.getSocialId());
        given(jwtTokenProvider.getSocialId(MalformedToken)).willReturn(testUser.getSocialId());
        willThrow(MalformedJwtException.class).given(jwtTokenProvider).getAuthentication(MalformedToken);
        request.setRequestURI("/test");
        request.setMethod("GET");
        assertThrows(MalformedJwtException.class,
                () -> jwtAuthenticationFilter.doFilterInternal(request, response, chain));
    }


}