package com.sollertia.habit.domain.user.follow.repository;

import com.sollertia.habit.TestConfig;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.dto.FollowDto;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(TestConfig.class)
class FollowRepositoryImplTest {

    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Autowired
    EntityManager em;

    User testUser;
    User testUser2;
    Monster monster;
    Monster monster2;
    Follow follow1;
    Follow follow2;


    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "blue.img");
        monsterDatabaseRepository.save(monsterDatabase1);
        monster = Monster.createNewMonster("blue", monsterDatabase1);
        testUser.updateMonster(monster);
        testUser.setMonsterCode("monsterCode");
        testUser = userRepository.save(testUser);
        em.flush();

        attributes.clear();
        attributes.put("sub", "1234");
        attributes.put("name", "tester2");
        attributes.put("email", "tester2.test.com");
        Oauth2UserInfo oauth2UserInfo2 = new GoogleOauth2UserInfo(attributes);
        testUser2 = User.create(oauth2UserInfo2);
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1, MonsterType.RED, "red.img");
        monsterDatabaseRepository.save(monsterDatabase2);
        monster2 = Monster.createNewMonster("red", monsterDatabase2);
        testUser2.updateMonster(monster2);
        testUser2.setMonsterCode("monsterCode2");
        testUser2 = userRepository.save(testUser2);
        em.flush();

        follow1 = Follow.create(testUser2, testUser);
        follow1 = followRepository.save(follow1);
        em.flush();
        follow2 = Follow.create(testUser, testUser2);
        follow2 = followRepository.save(follow2);
    }

    @DisplayName("사용자의 followers 가져오기")
    @Test
    void searchFollowersByUser(){

        List<FollowDto> followDtoList = followRepository.searchFollowersByUser(testUser);

        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getNickName()).isEqualTo(testUser2.getUsername());
        assertThat(followDtoList.get(0).getIsFollowed()).isEqualTo(true);
    }

    @DisplayName("특정 사용자의 followers 가져오기")
    @Test
    void searchFollowersBySelectUser(){

        List<FollowDto> followDtoList = followRepository.searchFollowersByUser(testUser, testUser2);

        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(followDtoList.get(0).getNickName()).isEqualTo(testUser.getUsername());
        assertThat(followDtoList.get(0).getIsFollowed()).isEqualTo(null);
    }

    @DisplayName("사용자의 followings 가져오기")
    @Test
    void searchFollowingsByUser(){

        List<FollowDto> followDtoList = followRepository.searchFollowingsByUser(testUser);

        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getNickName()).isEqualTo(testUser2.getUsername());
        assertThat(followDtoList.get(0).getIsFollowed()).isEqualTo(true);
    }
    @DisplayName("특정 사용자의 followings 가져오기")
    @Test
    void searchFollowingsSelectByUser(){

        List<FollowDto> followDtoList = followRepository.searchFollowingsByUser(testUser, testUser2);

        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(followDtoList.get(0).getNickName()).isEqualTo(testUser.getUsername());
        assertThat(followDtoList.get(0).getIsFollowed()).isEqualTo(null);
    }

    @DisplayName("사용자 검색")
    @Test
    void searchUser(){

        FollowDto followDto = followRepository.searchUser("monsterCode", testUser);

        assertThat(followDto.getMonsterCode()).isEqualTo(testUser.getMonsterCode());
        assertThat(followDto.getNickName()).isEqualTo(testUser.getUsername());
        assertThat(followDto.getIsFollowed()).isEqualTo(null);
    }

    @DisplayName("Following 수")
    @Test
    void countByFollower(){

        Long number = followRepository.countByFollower(testUser);

        assertThat(number).isEqualTo(1L);
    }

    @DisplayName("Follower 수")
    @Test
    void countByFollowing(){

        Long number = followRepository.countByFollowing(testUser);

        assertThat(number).isEqualTo(1L);
    }





}