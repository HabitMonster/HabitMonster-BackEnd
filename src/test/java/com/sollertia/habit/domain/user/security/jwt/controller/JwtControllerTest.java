package com.sollertia.habit.domain.user.security.jwt.controller;


import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JwtController.class)
@AutoConfigureMockMvc(addFilters = false)
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
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;
    String refreshToken = "abcd";
    String accessToken = "abcd";


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
    @DisplayName("accessToken 발급 완료")
    @Test
    void loginCheck() throws Exception {

        //given
        authenticated();
        given(jwtTokenProvider.requestRefreshToken(any())).willReturn(refreshToken);
        given(userRepository.findBySocialId(any())).willReturn(java.util.Optional.of(testUser));
        given(jwtTokenProvider.responseAccessToken(testUser)).willReturn(refreshToken);


        //when
        mvc.perform(get("/user/loginCheck")
                        .header("R-AUTH-TOKEN",refreshToken)).andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value("Issuance completed accessToken"))
                .andExpect(jsonPath("statusCode").value(200))
                .andExpect(jsonPath("accessToken").value(refreshToken))
                .andExpect(jsonPath("isFirstLogin").value(false));
    }

    @DisplayName("refreshToken 분실")
    @Test
    void notFoundRefreshToken() throws Exception {

        //given
        authenticated();

        //when
        mvc.perform(get("/user/loginCheck")
                        .header("R-AUTH-TOKEN",refreshToken)).andDo(print())
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("responseMessage").value("NotFound RefreshToken"))
                .andExpect(jsonPath("statusCode").value(400));
    }

    @DisplayName("User NotFound")
    @Test
    void notFoundUser() throws Exception {

        //given
        authenticated();
        given(jwtTokenProvider.requestRefreshToken(any())).willReturn(refreshToken);

        //when
        mvc.perform(get("/user/loginCheck")
                        .header("R-AUTH-TOKEN",refreshToken)).andDo(print())
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("responseMessage").value("NotFound User"))
                .andExpect(jsonPath("statusCode").value(404));
    }

    @DisplayName("User LoginCheck")
    @Test
    void userLoginCheck() throws Exception {

        //given
        authenticated();
        given(jwtTokenProvider.requestAccessToken(any())).willReturn(accessToken);

        //when
        mvc.perform(get("/user/check")
                        .header("A-AUTH-TOKEN",accessToken)).andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("responseMessage").value("IsLogin True"))
                .andExpect(jsonPath("isFirstLogin").value(false))
                .andExpect(jsonPath("isLogin").value(true))
                .andExpect(jsonPath("statusCode").value(200));
    }

    @DisplayName("NotFound AccessToken")
    @Test
    void userLoginCheckNotFoundAccessToken() throws Exception {

        //given
        authenticated();

        //when
        mvc.perform(get("/user/check")
                        .header("A-AUTH-TOKEN",accessToken)).andDo(print())
                //then
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("responseMessage").value("NotFound AccessToken"))
                .andExpect(jsonPath("statusCode").value(400));
    }
}