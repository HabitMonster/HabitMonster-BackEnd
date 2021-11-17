package com.sollertia.habit.domain.user.follow.service;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowCheckDto;
import com.sollertia.habit.domain.user.follow.dto.FollowResponseDto;
import com.sollertia.habit.domain.user.follow.dto.FollowSearchResponseDto;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.user.FollowException;
import com.sollertia.habit.global.exception.user.UserIdNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class FollowServiceImplTest {

    @InjectMocks
    private FollowServiceImpl followService;

    @Mock
    private FollowRepository followRepository;
    @Mock
    private UserRepository userRepository;

    User testUser;
    User testUser2;
    Monster monster;

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
        Whitebox.setInternalState(testUser, "id", 1L);


        attributes.clear();
        attributes.put("sub", "1234");
        attributes.put("name", "tester2");
        attributes.put("email", "tester2.test.com");
        Oauth2UserInfo oauth2UserInfo2 = new GoogleOauth2UserInfo(attributes);
        testUser2 = User.create(oauth2UserInfo2);
        testUser2.updateMonster(monster);
        Whitebox.setInternalState(testUser2, "id", 2L);
    }

    @DisplayName("Followers 가져오기")
    @Test
    void getFollowers() {
        //given
        Follow follow = Follow.create(testUser2, testUser);
        List<Follow> follows = new ArrayList<>();
        follows.add(follow);
        given(followRepository.findAllByFollowingId(testUser.getId())).willReturn(follows);
        given(userRepository.findBySocialId(testUser2.getSocialId())).willReturn(java.util.Optional.ofNullable(testUser2));
        lenient().when(followRepository.findByFollowerIdAndFollowingId(testUser2.getId(), testUser.getId())).thenReturn(follow);

        //when
        FollowResponseDto followResponseDto = followService.getFollowers(testUser);

        //then
        assertThat(followResponseDto.getFollowers().get(0).getIsFollowed()).isEqualTo(false);
        assertThat(followResponseDto.getFollowers().get(0).getEmail()).isEqualTo(testUser2.getEmail());
        assertThat(followResponseDto.getFollowers().get(0).getMonsterName()).isEqualTo(testUser2.getMonster().getName());
        assertThat(followResponseDto.getFollowers().get(0).getMonsterImg()).isEqualTo(testUser2.getMonster().getMonsterDatabase().getImageUrl());
        assertThat(followResponseDto.getFollowers().get(0).getMonsterCode()).isEqualTo(testUser2.getSocialId());
        assertThat(followResponseDto.getStatusCode()).isEqualTo(200);
        assertThat(followResponseDto.getResponseMessage()).isEqualTo("Followers Query Completed");
    }

    @DisplayName("Followeings 가져오기")
    @Test
    void getFollowings() {
        //given
        Follow follow = Follow.create(testUser, testUser2);
        List<Follow> follows = new ArrayList<>();
        follows.add(follow);
        given(followRepository.findAllByFollowerId(testUser.getId())).willReturn(follows);

        //when
        FollowResponseDto followResponseDto = followService.getFollowings(testUser);

        //then
        assertThat(followResponseDto.getFollowings().get(0).getEmail()).isEqualTo(testUser2.getEmail());
        assertThat(followResponseDto.getFollowings().get(0).getMonsterName()).isEqualTo(testUser2.getMonster().getName());
        assertThat(followResponseDto.getFollowings().get(0).getMonsterImg()).isEqualTo(testUser2.getMonster().getMonsterDatabase().getImageUrl());
        assertThat(followResponseDto.getFollowings().get(0).getMonsterCode()).isEqualTo(testUser2.getSocialId());
        assertThat(followResponseDto.getStatusCode()).isEqualTo(200);
        assertThat(followResponseDto.getResponseMessage()).isEqualTo("Followings Query Completed");
    }

    @DisplayName("Follow 하기")
    @Test
    void requestFollow() {
        //given
        Follow follow = Follow.create(testUser, testUser2);
        given(userRepository.findBySocialId(testUser2.getSocialId())).willReturn(java.util.Optional.ofNullable(testUser2));

        //when
        FollowCheckDto responseDto = followService.requestFollow("1234G", testUser);

        //then
        assertThat(responseDto.getIsFollowed()).isEqualTo(true);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Success Follow");
    }

    @DisplayName("Already Follow")
    @Test
    void alreadyFollow() {
        //given
        Follow follow = Follow.create(testUser, testUser2);
        given(userRepository.findBySocialId(testUser2.getSocialId())).willReturn(java.util.Optional.ofNullable(testUser2));
        given(followRepository.findByFollowerIdAndFollowingId(testUser.getId(), testUser2.getId())).willReturn(follow);

        //when - then
        assertThrows(FollowException.class,
                () -> followService.requestFollow("1234G", testUser));
    }

    @DisplayName("UnFollow 하기")
    @Test
    void requestUnFollow() {
        //given
        given(userRepository.findBySocialId(testUser2.getSocialId())).willReturn(java.util.Optional.ofNullable(testUser2));

        //when
        FollowCheckDto responseDto = followService.requestUnFollow("1234G", testUser);

        //then
        assertThat(responseDto.getIsFollowed()).isEqualTo(false);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Success UnFollow");
    }

    @DisplayName("searchFollowing")
    @Test
    void searchFollowing() {
        //given
        Follow follow = Follow.create(testUser2, testUser);
        given(userRepository.findBySocialId(testUser2.getSocialId())).willReturn(java.util.Optional.ofNullable(testUser2));
        lenient().when(followRepository.findByFollowerIdAndFollowingId(testUser2.getId(), testUser.getId())).thenReturn(follow);

        //when
        FollowSearchResponseDto responseDto = followService.searchFollowing("1234G", testUser);

        //then
        assertThat(responseDto.getSearchResult().getEmail()).isEqualTo(testUser2.getEmail());
        assertThat(responseDto.getSearchResult().getIsFollowed()).isEqualTo(false);
        assertThat(responseDto.getSearchResult().getMonsterCode()).isEqualTo(testUser2.getSocialId());
        assertThat(responseDto.getSearchResult().getMonsterImg()).isEqualTo(testUser2.getMonster().getMonsterDatabase().getImageUrl());
        assertThat(responseDto.getSearchResult().getMonsterName()).isEqualTo(testUser2.getMonster().getName());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Search Completed");
    }

    @DisplayName("Follow UserNoFound")
    @Test
    void followUserNotFound() {
        willThrow(UserIdNotFoundException.class).given(userRepository).findBySocialId("1234G");
        assertThrows(UserIdNotFoundException.class,
                () -> followService.requestFollow("1234G", testUser));
    }

    @DisplayName("UnFollow UserNoFound")
    @Test
    void unFollowUserNotFound() {
        willThrow(UserIdNotFoundException.class).given(userRepository).findBySocialId("1234G");
        assertThrows(UserIdNotFoundException.class,
                () -> followService.requestUnFollow("1234G", testUser));
    }

    @DisplayName("searchFollowing UserNoFound")
    @Test
    void searchFollowingUserNotFound() {
        willThrow(UserIdNotFoundException.class).given(userRepository).findBySocialId("1234G");
        assertThrows(UserIdNotFoundException.class,
                () -> followService.searchFollowing("1234G", testUser));
    }

    @DisplayName("checkFollow UserNoFound")
    @Test
    void checkFollowUserNotFound() {
        willThrow(UserIdNotFoundException.class).given(userRepository).findBySocialId("1234G");
        assertThrows(UserIdNotFoundException.class,
                () -> followService.checkFollow("1234G", testUser));
    }
}