package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitSummaryVo;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.dto.*;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.follow.dto.FollowCount;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;
import com.sollertia.habit.domain.user.follow.service.FollowServiceImpl;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.utils.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.reflect.Whitebox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private FollowServiceImpl followService;
    @Mock
    private HabitServiceImpl habitService;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private RandomUtil randomUtil;
    User testUser;
    User otherUser;
    User updatedTestUser;
    Monster monster1;
    Monster monster2;

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        testUser.setMonsterCode("monsterCode");
        updatedTestUser = User.create(oauth2UserInfo);

        Map<String, Object> attributes2 = new HashMap<>();
        attributes2.put("sub", "abcdefg");
        attributes2.put("name", "other");
        attributes2.put("email", "other.test.com");
        Oauth2UserInfo oauth2OtherInfo = new GoogleOauth2UserInfo(attributes2);
        otherUser = User.create(oauth2OtherInfo);
        otherUser.setMonsterCode("otherCode");

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1,MonsterType.RED, "dog.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);
        testUser.updateMonster(monster1);
        otherUser.updateMonster(monster2);
        Whitebox.setInternalState(monster1, "id", 1L);
        Whitebox.setInternalState(monster2, "id", 2L);
        Whitebox.setInternalState(monster1, "createdAt", LocalDateTime.now());
        Whitebox.setInternalState(monster2, "createdAt", LocalDateTime.now());
        Whitebox.setInternalState(testUser, "monsterCode", "testUser");
        Whitebox.setInternalState(otherUser, "monsterCode", "otherUser");
        updatedTestUser.updateMonster(monster2);
    }
    @Test
    void getUserInfoResponseDto() {
        //when
        UserInfoResponseDto responseDto = userService.getUserInfoResponseDto(testUser);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Info Query Completed");
        assertThat(responseDto.getUserInfo().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(responseDto.getUserInfo().getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(responseDto.getUserInfo().getMonsterName()).isEqualTo(testUser.getMonster().getName());
    }

    @Test
    void updateUsername() {
        //given
        UsernameUpdateRequestDto requestDto = new UsernameUpdateRequestDto();
        Whitebox.setInternalState(requestDto, "username", "modified");
        //when
        UserInfoResponseDto responseDto = userService.updateUsername(testUser, requestDto);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Name Updated Completed");
        assertThat(responseDto.getUserInfo().getUsername()).isEqualTo(requestDto.getUsername());
        assertThat(responseDto.getUserInfo().getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(responseDto.getUserInfo().getMonsterName()).isEqualTo(testUser.getMonster().getName());
    }

    @Test
    void disableUser() {
        //given
        //when
        UserInfoResponseDto responseDto = userService.disableUser(testUser);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Droped");
        assertThat(responseDto.getUserInfo().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(responseDto.getUserInfo().getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(responseDto.getUserInfo().getMonsterName()).isEqualTo(testUser.getMonster().getName());
    }

    @Test
    void getUserDetailDtoByMonsterCode() {
        //given
        UserMonsterDto userMonsterDto = new UserMonsterDto(
                otherUser.getMonsterCode(),
                otherUser.getUsername(),
                otherUser.getEmail(),
                monster2.getId(),
                monster2.getMonsterDatabase().getMonsterType(),
                monster2.getMonsterDatabase().getImageUrl(),
                monster2.getName(),
                monster2.getLevel(),
                monster2.getExpPoint(),
                monster2.getCreatedAt(),
                Boolean.FALSE,
                otherUser
        );
        given(userRepository.userDetailByMonsterCode(otherUser.getMonsterCode(), testUser))
                .willReturn(userMonsterDto);

        FollowCount followCount = new FollowCount(30L, 30L);
        given(followService.getCountByUser(otherUser))
                .willReturn(followCount);

        List<HabitSummaryVo> habitSummaryVoList = new ArrayList<>();
        HabitSummaryVo habitSummaryVo = HabitSummaryVo.builder()
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
        habitSummaryVoList.add(habitSummaryVo);
        given(habitService.getHabitListByUser(otherUser))
                .willReturn(habitSummaryVoList);

        //when
        UserDetailResponseDto responseDto = userService.getUserDetailDtoByMonsterCode(testUser, otherUser.getMonsterCode());

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Detail Response");
        assertThat(responseDto.getUserInfo().getMonsterCode()).isEqualTo(otherUser.getMonsterCode());
        assertThat(responseDto.getUserInfo().getUsername()).isEqualTo(otherUser.getUsername());
        assertThat(responseDto.getUserInfo().getEmail()).isEqualTo(otherUser.getEmail());
        assertThat(responseDto.getUserInfo().getIsFollowed()).isEqualTo(userMonsterDto.getIsFollowed());
        assertThat(responseDto.getUserInfo().getFollowersCount()).isEqualTo(followCount.getFollowersCount());
        assertThat(responseDto.getUserInfo().getFollowingsCount()).isEqualTo(followCount.getFollowingsCount());
        assertThat(responseDto.getMonster().getMonsterId()).isEqualTo(monster2.getId());
        assertThat(responseDto.getMonster().getMonsterImage()).isEqualTo(monster2.getMonsterDatabase().getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName()).isEqualTo(monster2.getName());
        assertThat(responseDto.getMonster().getMonsterLevel()).isEqualTo(monster2.getLevel().getValue());
        assertThat(responseDto.getMonster().getMonsterExpPoint()).isEqualTo(monster2.getExpPoint());
        assertThat(responseDto.getMonster().getCreateAt()).isEqualTo(monster2.getCreatedAt().toLocalDate().toString());
        assertThat(responseDto.getMonster().getCreateAt()).isEqualTo(monster2.getCreatedAt().toLocalDate().toString());
        assertThat(responseDto.getHabits().get(0).getHabitId()).isEqualTo(habitSummaryVo.getHabitId());
        assertThat(responseDto.getHabits().get(0).getTitle()).isEqualTo(habitSummaryVo.getTitle());
        assertThat(responseDto.getHabits().get(0).getDescription()).isEqualTo(habitSummaryVo.getDescription());
        assertThat(responseDto.getHabits().get(0).getDurationEnd()).isEqualTo(habitSummaryVo.getDurationEnd());
        assertThat(responseDto.getHabits().get(0).getDurationStart()).isEqualTo(habitSummaryVo.getDurationStart());
        assertThat(responseDto.getHabits().get(0).getCategoryId()).isEqualTo(habitSummaryVo.getCategoryId());
        assertThat(responseDto.getHabits().get(0).getCategory()).isEqualTo(habitSummaryVo.getCategory());
        assertThat(responseDto.getHabits().get(0).getCount()).isEqualTo(habitSummaryVo.getCount());
        assertThat(responseDto.getHabits().get(0).getCurrent()).isEqualTo(habitSummaryVo.getCurrent());
        assertThat(responseDto.getHabits().get(0).getIsAccomplished()).isEqualTo(habitSummaryVo.getIsAccomplished());
        assertThat(responseDto.getHabits().get(0).getPracticeDays()).isEqualTo(habitSummaryVo.getPracticeDays());
        assertThat(responseDto.getHabits().get(0).getAchieveCount()).isEqualTo(habitSummaryVo.getAchieveCount());
        assertThat(responseDto.getHabits().get(0).getTotalCount()).isEqualTo(habitSummaryVo.getTotalCount());

        verify(userRepository).userDetailByMonsterCode(otherUser.getMonsterCode(), testUser);
        verify(followService).getCountByUser(otherUser);
        verify(habitService).getHabitListByUser(otherUser);
    }

    @Test
    void getRecommendedUserListDto() {
        //given
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

        int number = 3;
        given(randomUtil.getRandomNumber()).willReturn(number);
        given(randomUtil.getRandomNumbers(recommendationDtoList.size()))
                .willReturn(new int[]{0,1,2,3,4});
        given(recommendationRepository.searchByNumber(testUser, number))
                .willReturn(recommendationDtoList);

        //when
        RecommendedUserListDto responseDto = userService.getRecommendedUserListDto(testUser);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Response Recommeded User List");
        assertThat(responseDto.getUserList().size()).isEqualTo(recommendationDtoList.size());
        assertThat(responseDto.getUserList().get(0).getTitle())
                .isEqualTo(recommendationDtoList.get(0).getTitle());
        assertThat(responseDto.getUserList().get(1).getTitle())
                .isEqualTo(recommendationDtoList.get(1).getTitle());
        assertThat(responseDto.getUserList().get(2).getTitle())
                .isEqualTo(recommendationDtoList.get(2).getTitle());
        assertThat(responseDto.getUserList().get(3).getTitle())
                .isEqualTo(recommendationDtoList.get(3).getTitle());
        assertThat(responseDto.getUserList().get(4).getTitle())
                .isEqualTo(recommendationDtoList.get(4).getTitle());
        assertThat(responseDto.getUserList().get(0).getUserInfo().getNickName())
                .isEqualTo(recommendationDtoList.get(0).getUserInfo().getNickName());
        assertThat(responseDto.getUserList().get(1).getUserInfo().getNickName())
                .isEqualTo(recommendationDtoList.get(1).getUserInfo().getNickName());
        assertThat(responseDto.getUserList().get(2).getUserInfo().getNickName())
                .isEqualTo(recommendationDtoList.get(2).getUserInfo().getNickName());
        assertThat(responseDto.getUserList().get(3).getUserInfo().getNickName())
                .isEqualTo(recommendationDtoList.get(3).getUserInfo().getNickName());
        assertThat(responseDto.getUserList().get(4).getUserInfo().getNickName())
                .isEqualTo(recommendationDtoList.get(4).getUserInfo().getNickName());

        verify(recommendationRepository).searchByNumber(testUser, number);
        verify(randomUtil).getRandomNumber();
        verify(randomUtil).getRandomNumbers(recommendationDtoList.size());
    }
}