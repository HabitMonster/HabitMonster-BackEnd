package com.sollertia.habit.domain.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitSummaryDto;
import com.sollertia.habit.domain.monster.dto.MonsterDto;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.domain.user.service.UserService;
import com.sollertia.habit.global.exception.user.InvalidRecommendationTypeException;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(PowerMockRunner.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

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
        testUser.setMonsterCode("monsterCode");
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
        UserInfoDto infoDto = UserInfoDto.builder()
                .monsterCode(testUser.getMonsterCode())
                .username(testUser.getUsername())
                .build();
        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .userInfo(infoDto)
                .statusCode(200)
                .responseMessage("User Info Query Completed")
                .build();
        given(userService.getUserInfoResponseDto(testUser))
                .willReturn(responseDto);

        //when
        mvc.perform(get("/user/info"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.userInfo.monsterCode").value(testUser.getMonsterCode()))
                .andExpect(jsonPath("$.responseMessage").value("User Info Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).getUserInfoResponseDto(testUser);
    }

    @Test
    void updateUsername() throws Exception {
        //given
        authenticated();
        UserInfoDto infoDto = UserInfoDto.builder()
                .monsterCode(testUser.getMonsterCode())
                .username("modified")
                .build();
        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .userInfo(infoDto)
                .statusCode(200)
                .responseMessage("User Name Updated Completed")
                .build();
        UsernameUpdateRequestDto requestDto = new UsernameUpdateRequestDto();
        Whitebox.setInternalState(requestDto, "username", "modified");
        String json = objectMapper.writeValueAsString(requestDto);

        given(userService.updateUsername(eq(testUser), any(UsernameUpdateRequestDto.class)))
                .willReturn(responseDto);

        //when
        mvc.perform(patch("/user/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.username").value(infoDto.getUsername()))
                .andExpect(jsonPath("$.userInfo.monsterCode").value(testUser.getMonsterCode()))
                .andExpect(jsonPath("$.responseMessage").value("User Name Updated Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).updateUsername(eq(testUser), any(UsernameUpdateRequestDto.class));
    }

    @Test
    void disableUser() throws Exception {
        //given
        authenticated();
        UserInfoDto infoDto = UserInfoDto.builder()
                .monsterCode(testUser.getMonsterCode())
                .username(testUser.getUsername())
                .build();
        UserInfoResponseDto responseDto = UserInfoResponseDto.builder()
                .userInfo(infoDto)
                .statusCode(200)
                .responseMessage("User Droped")
                .build();

        given(userService.disableUser(testUser)).willReturn(responseDto);

        //when
        mvc.perform(delete("/user"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.username").value(infoDto.getUsername()))
                .andExpect(jsonPath("$.userInfo.monsterCode").value(testUser.getMonsterCode()))
                .andExpect(jsonPath("$.responseMessage").value("User Droped"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).disableUser(testUser);
    }

    @Test
    void getUserInfoByMonsterCode() throws Exception {
        //given
        authenticated();
        UserDetailsDto userDetailsDto = UserDetailsDto.builder()
                .monsterCode(testUser.getMonsterCode())
                .username(testUser.getUsername())
                .email(testUser.getEmail())
                .isFollowed(Boolean.FALSE)
                .totalHabitCount(100)
                .followersCount(30L)
                .followingsCount(30L)
                .build();

        HabitSummaryDto habitSummaryDto = HabitSummaryDto.builder()
                .habitId(3L)
                .title("test")
                .description("test")
                .durationStart("2021-11-11")
                .durationEnd("2021-11-30")
                .count(3)
                .current(0)
                .isAccomplished(Boolean.FALSE)
                .practiceDays("1234567")
                .achievePercentage(40L)
                .category(Category.Etc)
                .categoryId(Category.Etc.getCategoryId())
                .achieveCount(15)
                .totalCount(30)
                .build();
        List<HabitSummaryDto> habitSummaryDtoList = new ArrayList<>();
        habitSummaryDtoList.add(habitSummaryDto);

        MonsterDto monsterDto = MonsterDto.builder()
                .monsterId(30L)
                .levelOneId(null)
                .monsterImage("test.img")
                .monsterName("test")
                .monsterLevel(3)
                .monsterExpPoint(30L)
                .createAt("2021-11-11")
                .build();

        UserDetailResponseDto responseDto = UserDetailResponseDto.builder()
                .statusCode(200)
                .responseMessage("User Detail Response")
                .userInfo(userDetailsDto)
                .habits(habitSummaryDtoList)
                .monster(monsterDto)
                .build();

        given(userService.getUserDetailDtoByMonsterCode(testUser, testUser.getMonsterCode()))
                .willReturn(responseDto);

        //when
        mvc.perform(get("/user/"+testUser.getMonsterCode()+"/info"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userInfo.username").value(testUser.getUsername()))
                .andExpect(jsonPath("$.userInfo.monsterCode").value(testUser.getMonsterCode()))
                .andExpect(jsonPath("$.userInfo.email").value(testUser.getEmail()))
                .andExpect(jsonPath("$.userInfo.isFollowed").value(userDetailsDto.getIsFollowed()))
                .andExpect(jsonPath("$.userInfo.totalHabitCount").value(userDetailsDto.getTotalHabitCount()))
                .andExpect(jsonPath("$.monster.monsterId").value(monsterDto.getMonsterId()))
                .andExpect(jsonPath("$.monster.monsterImage").value(monsterDto.getMonsterImage()))
                .andExpect(jsonPath("$.monster.monsterName").value(monsterDto.getMonsterName()))
                .andExpect(jsonPath("$.monster.monsterLevel").value(monsterDto.getMonsterLevel()))
                .andExpect(jsonPath("$.habits[0].habitId").value(habitSummaryDto.getHabitId()))
                .andExpect(jsonPath("$.habits[0].categoryId").value(habitSummaryDto.getCategoryId()))
                .andExpect(jsonPath("$.habits[0].count").value(habitSummaryDto.getCount()))
                .andExpect(jsonPath("$.habits[0].current").value(habitSummaryDto.getCurrent()))
                .andExpect(jsonPath("$.habits[0].achieveCount").value(habitSummaryDto.getAchieveCount()))
                .andExpect(jsonPath("$.habits[0].title").value(habitSummaryDto.getTitle()))
                .andExpect(jsonPath("$.habits[0].description").value(habitSummaryDto.getDescription()))
                .andExpect(jsonPath("$.habits[0].durationStart").value(habitSummaryDto.getDurationStart()))
                .andExpect(jsonPath("$.habits[0].durationEnd").value(habitSummaryDto.getDurationEnd()))
                .andExpect(jsonPath("$.responseMessage").value("User Detail Response"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).getUserDetailDtoByMonsterCode(testUser, testUser.getMonsterCode());
    }

    @Test
    void getRecommendedUsers() throws Exception {
        //given
        authenticated();
        List<RecommendationDto> recommendationDtoList = new ArrayList<>();
        recommendationDtoList.add(new RecommendationDto(RecommendationType.CREATE_TOP10,
                        new FollowDto("test1", 1L, "test.img", "code1", Boolean.FALSE)));
        recommendationDtoList.add(new RecommendationDto(RecommendationType.EMOTION_TOP10,
                new FollowDto("test2", 2L, "test.img", "code2", Boolean.FALSE)));
        recommendationDtoList.add(new RecommendationDto(RecommendationType.FOLLOWERS_TOP10,
                new FollowDto("test3", 3L, "test.img", "code3", Boolean.FALSE)));
        recommendationDtoList.add(new RecommendationDto(RecommendationType.LIFE_TOP10,
                new FollowDto("test4", 4L, "test.img", "code4", Boolean.FALSE)));
        recommendationDtoList.add(new RecommendationDto(RecommendationType.HEALTH_TOP10,
                new FollowDto("test5", 5L, "test.img", "code5", Boolean.FALSE)));

        RecommendedUserListDto responseDto = RecommendedUserListDto.builder()
                .userList(recommendationDtoList)
                .statusCode(200)
                .responseMessage("Response Recommeded User List")
                .build();

        given(userService.getRecommendedUserListDto(testUser)).willReturn(responseDto);

        //when
        mvc.perform(get("/users/recommended"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userList[0].title").value(recommendationDtoList.get(0).getTitle()))
                .andExpect(jsonPath("$.userList[1].title").value(recommendationDtoList.get(1).getTitle()))
                .andExpect(jsonPath("$.userList[2].title").value(recommendationDtoList.get(2).getTitle()))
                .andExpect(jsonPath("$.userList[3].title").value(recommendationDtoList.get(3).getTitle()))
                .andExpect(jsonPath("$.userList[4].title").value(recommendationDtoList.get(4).getTitle()))
                .andExpect(jsonPath("$.userList[0].userInfo.nickName").value(recommendationDtoList.get(0).getUserInfo().getNickName()))
                .andExpect(jsonPath("$.userList[0].userInfo.monsterId").value(recommendationDtoList.get(0).getUserInfo().getMonsterId()))
                .andExpect(jsonPath("$.userList[0].userInfo.monsterImg").value(recommendationDtoList.get(0).getUserInfo().getMonsterImg()))
                .andExpect(jsonPath("$.userList[0].userInfo.monsterCode").value(recommendationDtoList.get(0).getUserInfo().getMonsterCode()))
                .andExpect(jsonPath("$.userList[0].userInfo.isFollowed").value(recommendationDtoList.get(0).getUserInfo().getIsFollowed()))
                .andExpect(jsonPath("$.userList[1].userInfo.nickName").value(recommendationDtoList.get(1).getUserInfo().getNickName()))
                .andExpect(jsonPath("$.userList[1].userInfo.monsterId").value(recommendationDtoList.get(1).getUserInfo().getMonsterId()))
                .andExpect(jsonPath("$.userList[1].userInfo.monsterImg").value(recommendationDtoList.get(1).getUserInfo().getMonsterImg()))
                .andExpect(jsonPath("$.userList[1].userInfo.monsterCode").value(recommendationDtoList.get(1).getUserInfo().getMonsterCode()))
                .andExpect(jsonPath("$.userList[1].userInfo.isFollowed").value(recommendationDtoList.get(1).getUserInfo().getIsFollowed()))
                .andExpect(jsonPath("$.userList[2].userInfo.nickName").value(recommendationDtoList.get(2).getUserInfo().getNickName()))
                .andExpect(jsonPath("$.userList[2].userInfo.monsterId").value(recommendationDtoList.get(2).getUserInfo().getMonsterId()))
                .andExpect(jsonPath("$.userList[2].userInfo.monsterImg").value(recommendationDtoList.get(2).getUserInfo().getMonsterImg()))
                .andExpect(jsonPath("$.userList[2].userInfo.monsterCode").value(recommendationDtoList.get(2).getUserInfo().getMonsterCode()))
                .andExpect(jsonPath("$.userList[2].userInfo.isFollowed").value(recommendationDtoList.get(2).getUserInfo().getIsFollowed()))

                .andExpect(jsonPath("$.responseMessage").value("Response Recommeded User List"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(userService).getRecommendedUserListDto(testUser);
    }

    @Test
    void getRecommendedUsersEmpty() throws Exception {
        //given
        authenticated();
        given(userService.getRecommendedUserListDto(testUser))
                .willThrow(new InvalidRecommendationTypeException("Recommendations List is Empty"));

        //when
        mvc.perform(get("/users/recommended"))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseMessage").value("Recommendations List is Empty"))
                .andExpect(jsonPath("$.statusCode").value("404"));

        verify(userService).getRecommendedUserListDto(testUser);
    }
}