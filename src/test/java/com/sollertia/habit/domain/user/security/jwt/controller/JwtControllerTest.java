package com.sollertia.habit.domain.user.security.jwt.controller;


import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.domain.user.security.jwt.dto.JwtRequestDto;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JwtController.class)
@ExtendWith(MockitoExtension.class)
class JwtControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationManager authenticationManager;


    User testUser;
    JwtRequestDto requestDto;
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;
    Authentication authentication;
    MockHttpServletRequest request = new MockHttpServletRequest();
    public static final String signatureToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0RyIsInR5cGUiOiJHb29nbGUiLCJpYXQiOjE2MzYwMjQ4NzcsImV4cCI6MTYzNjAyNDg4Mn0.OAXTkojY2evdc-NsU2Za0Bv6VaWsv0FQVlhSt7QhxbY";

    public static final String expiredToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxNjM2MDM1ODI3fQ.zI4KZQPw_L8yEH4XdsKYMF5ZDaY5k-IzFYkWA9wEgas";

    private static final String successToken =
            "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODlHIiwidHlwZSI6Ikdvb2dsZSIsImlhdCI6MTYzNjAzNTgyNiwiZXhwIjoxODg2MzYyODY2fQ.qHqotXvz0vipsiQmHiS3utO-43yop98fdUK9XcMXXWQ";

    private static final String MalformedToken = "abcdefg";

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        mockUserDetails = new UserDetailsImpl(testUser);
    }
//    @DisplayName("accessToken 발급 완료")
//    @Test
//    void loginCheck() throws Exception {
//
//        String refreshToken = "{\n" +
//                " \"refreshToken\" : \"" + successToken + "\", \n" +
//                "}";
//        //given
//        authenticated();
//        given(requestDto.getRefreshToken()).willReturn(successToken);
//        given(jwtTokenProvider.getSocialId(successToken)).willReturn(testUser.getSocialId());
//        given(redisUtil.getData(successToken)).willReturn(testUser.getSocialId());
//        given(userRepository.findBySocialId(successToken)).willReturn(java.util.Optional.ofNullable(testUser));
//        given(jwtTokenProvider.responseAccessToken(testUser)).willReturn(successToken);

//        willThrow(ExpiredJwtException.class).given(jwtTokenProvider).validateToken(expiredToken);
//        given(jwtTokenProvider.getAuthentication(successToken)).willReturn(authentication);
        //when
//        mvc.perform(post("/user/loginCheck")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(refreshToken)).andDo(print())
//                //then
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("responseMessage").value("accessToken 발급완료!"))
//                .andExpect(jsonPath("statusCode").value(200))
//                .andExpect(jsonPath("accessToken").value(successToken));
//    }
}