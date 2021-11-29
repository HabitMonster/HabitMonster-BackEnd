package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.TestConfig;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.user.dto.RecommendationDto;
import com.sollertia.habit.domain.user.entity.Recommendation;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.enums.RecommendationType;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(TestConfig.class)
class RecommendationRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private RecommendationRepository recommendationRepository;
    @Autowired
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Autowired
    EntityManager em;

    User testUser;
    User testUser2;
    Monster monster;
    Monster monster2;
    Follow follow;

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

        follow = Follow.create(testUser2, testUser);
        follow = followRepository.save(follow);

        List<User> userList = new ArrayList<>();
        userList.add(testUser);

        List<Recommendation> recommendations = Recommendation.listOf(userList, RecommendationType.HEALTH_TOP10);
        recommendationRepository.save(recommendations.get(0));
    }

    @DisplayName("추천 유저 조회")
    @Test
    void searchByNumber(){

        List<RecommendationDto> recommendationDtos =  recommendationRepository.searchByNumber(testUser2, 0);

        assertThat(recommendationDtos.get(0).getTitle()).isEqualTo(RecommendationType.HEALTH_TOP10.getTitle());
        assertThat(recommendationDtos.get(0).getUserInfo().getIsFollowed()).isEqualTo(true);
        assertThat(recommendationDtos.get(0).getUserInfo().getNickName()).isEqualTo(testUser.getUsername());
        assertThat(recommendationDtos.get(0).getUserInfo().getMonsterId()).isEqualTo(testUser.getMonster().getMonsterDatabase().getId());
        assertThat(recommendationDtos.get(0).getUserInfo().getMonsterImg()).isEqualTo(testUser.getMonster().getMonsterDatabase().getImageUrl());
        assertThat(recommendationDtos.get(0).getUserInfo().getMonsterCode()).isEqualTo("monsterCode");

    }


}