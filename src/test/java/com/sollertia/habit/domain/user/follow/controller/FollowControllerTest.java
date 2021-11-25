package com.sollertia.habit.domain.user.follow.controller;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.*;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
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
@RunWith(PowerMockRunner.class)
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
        testUser2.setMonsterCode("monsterCode2");
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
        FollowVo followerVo = new FollowVo(testUser2.getUsername(),1L, testUser2.getMonster().getMonsterDatabase().getImageUrl(),
                testUser2.getMonsterCode(),true);
        List<FollowVo> followVos = new ArrayList<>();
        followVos.add(followerVo);
        FollowResponseDto followResponseDto = FollowResponseDto.builder().followers(followVos).statusCode(200).responseMessage("Followers Query Completed").build();
        given(followService.getFollowers(testUser)).willReturn(followResponseDto);

        //when
        mvc.perform(get("/followers"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followers[0].nickName").value("tester2"))
                .andExpect(jsonPath("$.followers[0].monsterId").value(1L))
                .andExpect(jsonPath("$.followers[0].monsterImg").value("blue.img"))
                .andExpect(jsonPath("$.followers[0].monsterCode").value("monsterCode2"))
                .andExpect(jsonPath("$.followers[0].isFollowed").value("true"))
                .andExpect(jsonPath("$.responseMessage").value("Followers Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).getFollowers(testUser);
    }

    @DisplayName("Followings 가져오기")
    @Test
    void getFollowings() throws Exception {
        //given
        authenticated();
        FollowVo followingVo = new FollowVo(testUser2.getUsername(),1L, testUser2.getMonster().getMonsterDatabase().getImageUrl(),
                testUser2.getMonsterCode(),true);
        List<FollowVo> followVos = new ArrayList<>();
        followVos.add(followingVo);
        FollowResponseDto followResponseDto = FollowResponseDto.builder().followings(followVos).statusCode(200).responseMessage("Followings Query Completed").build();
        given(followService.getFollowings(testUser)).willReturn(followResponseDto);

        //when
        mvc.perform(get("/followings"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.followings[0].nickName").value("tester2"))
                .andExpect(jsonPath("$.followings[0].monsterId").value(1L))
                .andExpect(jsonPath("$.followings[0].monsterImg").value("blue.img"))
                .andExpect(jsonPath("$.followings[0].monsterCode").value("monsterCode2"))
                .andExpect(jsonPath("$.responseMessage").value("Followings Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).getFollowings(testUser);
    }

    @DisplayName("Follow 하기")
    @Test
    void requestFollow() throws Exception {
        //given
        authenticated();
        FollowCheckDto responseDto = FollowCheckDto.builder().isFollowed(true).statusCode(200).responseMessage("Success Follow").build();
        given(followService.requestFollow("1234G", testUser)).willReturn(responseDto);

        //when
        mvc.perform(patch("/follow/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFollowed").value("true"))
                .andExpect(jsonPath("$.responseMessage").value("Success Follow"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).requestFollow("1234G", testUser);
    }

    @DisplayName("UnFollow 하기")
    @Test
    void requestUnFollow() throws Exception {
        //given
        authenticated();
        FollowCheckDto responseDto = FollowCheckDto.builder().isFollowed(false).statusCode(200).responseMessage("Success UnFollow").build();
        given(followService.requestUnFollow("1234G", testUser)).willReturn(responseDto);

        //when
        mvc.perform(delete("/unFollow/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFollowed").value("false"))
                .andExpect(jsonPath("$.responseMessage").value("Success UnFollow"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).requestUnFollow("1234G", testUser);
    }

    @DisplayName("친구 검색")
    @Test
    void searchFollowing() throws Exception {
        //given
        authenticated();
        FollowVo followVo = new FollowVo(testUser2.getUsername(),1L, testUser2.getMonster().getMonsterDatabase().getImageUrl(),
                testUser2.getMonsterCode(),false);
        FollowSearchResponseDto responseDto = FollowSearchResponseDto.builder().userInfo(followVo).statusCode(200).responseMessage("Search Completed").build();
        given(followService.searchFollowing("1234G",testUser)).willReturn(responseDto);

        //when
        mvc.perform(get("/user/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.monsterImg").value(testUser2.getMonster().getMonsterDatabase().getImageUrl()))
                .andExpect(jsonPath("$.userInfo.monsterCode").value(testUser2.getMonsterCode()))
                .andExpect(jsonPath("$.userInfo.monsterId").value("1"))
                .andExpect(jsonPath("$.userInfo.nickName").value(testUser2.getUsername()))
                .andExpect(jsonPath("$.userInfo.isFollowed").value("false"))
                .andExpect(jsonPath("$.responseMessage").value("Search Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).searchFollowing("1234G", testUser);
    }

    @DisplayName("Follow 유무 확인")
    @Test
    void checkFollow() throws Exception {
        //given
        authenticated();
        FollowCheckDto responseDto = FollowCheckDto.builder().isFollowed(true).statusCode(200).responseMessage("isFollowedTrue").build();
        given(followService.checkFollow("1234G",testUser)).willReturn(responseDto);

        //when
        mvc.perform(get("/checkFollow/1234G"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isFollowed").value("true"))
                .andExpect(jsonPath("$.responseMessage").value("isFollowedTrue"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(followService).checkFollow("1234G", testUser);
    }




}