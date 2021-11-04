package com.sollertia.habit.domain.oauth2;

import com.sollertia.habit.config.jwt.JwtTokenProvider;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = Oauth2Controller.class)
@AutoConfigureMockMvc(addFilters = false)
class Oauth2ControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private Oauth2UserService oauth2UserService;
    @MockBean
    private SocialLoginService socialLoginService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    private User testUser;
    private Oauth2UserInfo mockUserInfo;

    @BeforeEach
    void before() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        mockUserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(mockUserInfo);
        mockUserInfo.putUser(testUser);
    }

    @Test
    void login() throws Exception {
        //given
        String code = "abcdefg1234567";
        String socialName = "google";
        String mockAccessToken = "accessToken";
        String mockRefreshToken = "refreshToken";

        given(socialLoginService.getUserInfo(socialName, code))
                .willReturn(mockUserInfo);
        given(oauth2UserService.putUserInto(mockUserInfo))
                .willReturn(mockUserInfo);
        given(jwtTokenProvider.responseAccessToken(testUser))
                .willReturn(mockAccessToken);
        given(jwtTokenProvider.responseRefreshToken(testUser))
                .willReturn(mockRefreshToken);

        //when
        mvc.perform(get("/user/login/google")
                        .param("code", code))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFirstLogin").value(false))
                .andExpect(jsonPath("$.refreshToken").value(mockRefreshToken))
                .andExpect(jsonPath("$.accessToken").value(mockAccessToken))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("토큰 발급 완료"));

        verify(socialLoginService).getUserInfo(socialName, code);
        verify(oauth2UserService).putUserInto(mockUserInfo);
        verify(jwtTokenProvider).responseAccessToken(testUser);
        verify(jwtTokenProvider).responseRefreshToken(testUser);
    }

    @Test
    void register() throws Exception {
        //given
        mockUserInfo.toFirstLogin();
        String code = "abcdefg1234567";
        String socialName = "google";
        String mockAccessToken = "accessToken";
        String mockRefreshToken = "refreshToken";

        given(socialLoginService.getUserInfo(socialName, code))
                .willReturn(mockUserInfo);
        given(oauth2UserService.putUserInto(mockUserInfo))
                .willReturn(mockUserInfo);
        given(jwtTokenProvider.responseAccessToken(testUser))
                .willReturn(mockAccessToken);
        given(jwtTokenProvider.responseRefreshToken(testUser))
                .willReturn(mockRefreshToken);

        //when
        mvc.perform(get("/user/login/google")
                        .param("code", code))
                .andDo(print())

                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFirstLogin").value(true))
                .andExpect(jsonPath("$.refreshToken").value(mockRefreshToken))
                .andExpect(jsonPath("$.accessToken").value(mockAccessToken))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.responseMessage").value("토큰 발급 완료"));

        verify(socialLoginService).getUserInfo(socialName, code);
        verify(oauth2UserService).putUserInto(mockUserInfo);
        verify(jwtTokenProvider).responseAccessToken(testUser);
        verify(jwtTokenProvider).responseRefreshToken(testUser);
    }
}