package com.sollertia.habit.domain.user.follow.controller;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.dto.FollowVo;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FollowController.class)
class FollowControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FollowServiceImpl followService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    User testUser;
    User testUser2;
    Monster monster;
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
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "blue.img");
        monster = Monster.createNewMonster("blue", monsterDatabase1);
        testUser.updateMonster(monster);
        mockUserDetails = new UserDetailsImpl(testUser);

        attributes.clear();
        attributes.put("sub", "1234");
        attributes.put("name", "tester2");
        attributes.put("email", "tester2.test.com");
        Oauth2UserInfo oauth2UserInfo2 = new GoogleOauth2UserInfo(attributes);
        testUser2 = User.create(oauth2UserInfo2);
        testUser2.updateMonster(monster);
    }

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @DisplayName("Followers 가져오기")
    @Test
    void getFollowers() throws Exception {
        //given
        authenticated();
        Follow follow = Follow.create(testUser2,testUser);
        FollowVo followerVo = FollowVo.followerOf(follow);
        List<FollowVo> followVos = new ArrayList<>();
        followVos.add(followerVo);
        FollowResponseDto followResponseDto = FollowResponseDto.builder().followers(followVos).statusCode(200).responseMessage("Followers Query Completed").build();
        given(followService.getFollowers(testUser)).willReturn(followResponseDto);

        //when
        mvc.perform(get("/followers"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers[0].email").value("tester2.test.com"))
                .andExpect(jsonPath("$.followers[0].monsterName").value("blue"))
                .andExpect(jsonPath("$.followers[0].monsterImg").value("blue.img"))
                .andExpect(jsonPath("$.followers[0].monsterCode").value("1234G"))
                .andExpect(jsonPath("$.responseMessage").value("Followers Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).getFollowers(testUser);
    }

    @DisplayName("Followings 가져오기")
    @Test
    void getFollowings() throws Exception {
        //given
        authenticated();
        Follow follow = Follow.create(testUser,testUser2);
        FollowVo followingVo = FollowVo.followingOf(follow);
        List<FollowVo> followVos = new ArrayList<>();
        followVos.add(followingVo);
        FollowResponseDto followResponseDto = FollowResponseDto.builder().followings(followVos).statusCode(200).responseMessage("Followings Query Completed").build();
        given(followService.getFollowings(testUser)).willReturn(followResponseDto);

        //when
        mvc.perform(get("/followings"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followings[0].email").value("tester2.test.com"))
                .andExpect(jsonPath("$.followings[0].monsterName").value("blue"))
                .andExpect(jsonPath("$.followings[0].monsterImg").value("blue.img"))
                .andExpect(jsonPath("$.followings[0].monsterCode").value("1234G"))
                .andExpect(jsonPath("$.responseMessage").value("Followings Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).getFollowings(testUser);
    }

    @DisplayName("Follow 하기")
    @Test
    void requestFollow() throws Exception {
        //given
        authenticated();
        DefaultResponseDto responseDto = DefaultResponseDto.builder().statusCode(200).responseMessage("Success Follow").build();
        given(followService.requestFollow("1234G",testUser)).willReturn(responseDto);

        //when
        mvc.perform(patch("/follow/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Success Follow"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).requestFollow("1234G", testUser);
    }

    @DisplayName("UnFollow 하기")
    @Test
    void requestUnFollow() throws Exception {
        //given
        authenticated();
        DefaultResponseDto responseDto = DefaultResponseDto.builder().statusCode(200).responseMessage("Success UnFollow").build();
        given(followService.requestUnFollow("1234G",testUser)).willReturn(responseDto);

        //when
        mvc.perform(delete("/unFollow/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value("Success UnFollow"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).requestUnFollow("1234G", testUser);
    }




}