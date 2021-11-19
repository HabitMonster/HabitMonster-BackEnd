package com.sollertia.habit.domain.user.service;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.user.dto.UserInfoResponseDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    User testUser;
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

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1,MonsterType.RED, "dog.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);
        testUser.updateMonster(monster1);
        updatedTestUser.updateMonster(monster2);
    }
    @Test
    void getUserInfoResponseDto() {
        //when
        UserInfoResponseDto responseDto = userService.getUserInfoResponseDto(testUser);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Info Query Completed");
        assertThat(responseDto.getUserInfo().getEmail()).isEqualTo(testUser.getEmail());
        assertThat(responseDto.getUserInfo().getUsername()).isEqualTo(testUser.getUsername());
        assertThat(responseDto.getUserInfo().getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(responseDto.getUserInfo().getSocialType()).isEqualTo(testUser.getProviderType());
    }

    @Test
    void updateMonster() {
        //given
        given(userRepository.save(testUser)).willReturn(updatedTestUser);

        //when
        User user = userService.updateMonster(testUser, monster2);

        //then
        assertThat(user.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(user.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(user.getSocialId()).isEqualTo(testUser.getSocialId());
        assertThat(user.getMonster()).isEqualTo(testUser.getMonster());
        assertThat(user.getProviderType()).isEqualTo(testUser.getProviderType());
    }
}