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
        monsterDatabaseRepository.save(monsterDatabase1);
        monster = Monster.createNewMonster("blue", monsterDatabase1);
        testUser.updateMonster(monster);
        Whitebox.setInternalState(testUser, "monsterCode", "monsterCode");
        Whitebox.setInternalState(testUser, "id", 1L);
        userRepository.save(testUser);

        attributes.clear();
        attributes.put("sub", "1234");
        attributes.put("name", "tester2");
        attributes.put("email", "tester2.test.com");
        Oauth2UserInfo oauth2UserInfo2 = new GoogleOauth2UserInfo(attributes);
        testUser2 = User.create(oauth2UserInfo2);
        testUser2.updateMonster(monster);
        testUser2.setMonsterCode("monsterCode2");
        Whitebox.setInternalState(testUser2, "monsterCode", "monsterCode2");
        Whitebox.setInternalState(testUser2, "id", 2L);
        userRepository.save(testUser2);
    }

    @DisplayName("사용자의 followers 가져오기")
    @Test
    void searchFollowersByUser(){

        Follow follow = Follow.create(testUser2, testUser);
        Whitebox.setInternalState(follow, "id", 1L);
        follow = followRepository.save(follow);

        assertThat(follow.getFollower().getId()).isEqualTo(testUser2.getId());
        assertThat(follow.getFollowing().getId()).isEqualTo(testUser.getId());

        List<FollowDto> followDtoList = followRepository.searchFollowersByUser(testUser);

        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
        assertThat(followDtoList.get(0).getMonsterCode()).isEqualTo(testUser2.getMonsterCode());
    }


}