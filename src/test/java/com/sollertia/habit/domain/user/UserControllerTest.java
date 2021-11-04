package com.sollertia.habit.domain.user;

import com.sollertia.habit.config.jwt.JwtTokenProvider;
import com.sollertia.habit.domain.monster.MonsterController;
import com.sollertia.habit.domain.monster.MonsterService;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.dto.UserInfoVo;
import com.sollertia.habit.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    User testUser;
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;

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

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void getUserInfo() throws Exception {
        //given
        authenticated();
        UserInfoVo infoVo = UserInfoVo.builder()
                .socialId(testUser.getSocialId())
                .email(testUser.getEmail())
                .username(testUser.getUsername())
                .expPercentage(testUser.getExpPoint())
                .level(testUser.getLevel().getValue())
                .socialType(testUser.getType())
                .build();
        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .userInfo(infoVo)
                .statusCode(200)
                .responseMessage("사용자 정보 조회 성공")
                .build();
        given(userService.getUserInfoResponseDto(testUser))
                .willReturn(responseDto);

        //when
        mvc.perform(get("/user/info"))
                .andDo(print())
                //then
                .andExpect(jsonPath("$.userInfo.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.userInfo.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.userInfo.socialId").value(testUser.getSocialId()))
                .andExpect(jsonPath("$.responseMessage").value("사용자 정보 조회 성공"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).getUserInfoResponseDto(testUser);
    }
}