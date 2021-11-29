package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.TestConfig;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaConfig.class
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(TestConfig.class)
class MonsterCollectionRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Autowired
    private MonsterCollectionRepository monsterCollectionRepository;
    @Autowired
    private MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;
    @Autowired
    EntityManager em;

    User testUser;
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
        monsterDatabase1 = monsterDatabaseRepository.save(monsterDatabase1);
        monster = Monster.createNewMonster("blue", monsterDatabase1);
        testUser.updateMonster(monster);
        testUser.setMonsterCode("monsterCode");
        testUser = userRepository.save(testUser);
        em.flush();
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(monster);
        monsterCollection = monsterCollectionRepository.save(monsterCollection);
        em.flush();
        MonsterCollectionDatabase monsterCollectionDatabase = MonsterCollectionDatabase.from(monsterDatabase1, monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);
    }

    @DisplayName("사용자 몬스터 도감 조회")
    @Test
    void searchByUser(){
        List<MonsterCollection> monsterCollections = monsterCollectionRepository.searchByUser(testUser);

        assertThat(monsterCollections.get(0).getMonsterName()).isEqualTo("blue");
        assertThat(monsterCollections.get(0).getUser()).isEqualTo(testUser);
        assertThat(monsterCollections.get(0).getMaxLevel()).isEqualTo(monster.getLevel());
        assertThat(monsterCollections.get(0).getMonsterType()).isEqualTo(MonsterType.BLUE);
        assertThat(monsterCollections.get(0).getMonsterName()).isEqualTo(monster.getName());
        assertThat(monsterCollections.get(0).getCreatedAt()).isEqualTo(LocalDateTime.now().toLocalDate().toString());
    }

    @DisplayName("사용자 몬스터 도감에서 몬스터 타입 가져오기")
    @Test
    void searchTypeListByUser(){
        List<MonsterType> monsterTypes = monsterCollectionRepository.searchTypeListByUser(testUser);

        assertThat(monsterTypes.get(0)).isEqualTo(monster.getMonsterDatabase().getMonsterType());
    }


}