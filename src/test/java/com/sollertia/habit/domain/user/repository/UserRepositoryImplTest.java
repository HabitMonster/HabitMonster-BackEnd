package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.TestConfig;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.user.dto.UserMonsterDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.entity.Follow;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
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
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = JpaConfig.class
))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Import(TestConfig.class)
class UserRepositoryImplTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FollowRepository followRepository;
    @Autowired
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Autowired
    private CompletedHabitRepository completedHabitRepository;
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

        follow = Follow.create(testUser2, testUser);
        follow = followRepository.save(follow);

        HabitDtoImpl habitDto = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(6l)
                .practiceDays("1234567")
                .count(10)
                .build();

        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
        Habit habit = Habit.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
        CompletedHabit completedHabit = CompletedHabit.of(habit);
        completedHabitRepository.save(completedHabit);

        HabitDtoImpl habitDto2 = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(4l)
                .practiceDays("1234567")
                .count(10)
                .build();

        Habit habit2 = Habit.createHabit(habitTypeDto.getHabitType(), habitDto2, testUser2);
        CompletedHabit completedHabit2 = CompletedHabit.of(habit2);
        completedHabitRepository.save(completedHabit2);

        HabitDtoImpl habitDto3 = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(3l)
                .practiceDays("1234567")
                .count(10)
                .build();

        Habit habit3 = Habit.createHabit(habitTypeDto.getHabitType(), habitDto3, testUser2);
        CompletedHabit completedHabit3 = CompletedHabit.of(habit3);
        completedHabitRepository.save(completedHabit3);
    }

    @DisplayName("특정유저 상세 정보 가져오기")
    @Test
    void userDetailByMonsterCode(){

        UserMonsterDto userMonsterDto = userRepository.userDetailByMonsterCode("monsterCode2", testUser);

        assertThat(userMonsterDto.getMonsterCode()).isEqualTo("monsterCode2");
        assertThat(userMonsterDto.getUsername()).isEqualTo(testUser2.getUsername());
        assertThat(userMonsterDto.getEmail()).isEqualTo("tester2.test.com");
        assertThat(userMonsterDto.getLevelOneId()).isEqualTo(MonsterType.RED.getLv1Id());
        assertThat(userMonsterDto.getMonsterImage()).isEqualTo("red.img");
        assertThat(userMonsterDto.getMonsterName()).isEqualTo("red");
        assertThat(userMonsterDto.getMonsterLevel()).isEqualTo(Level.LV1.getValue());
        assertThat(userMonsterDto.getCreateAt()).isEqualTo(LocalDateTime.now().toLocalDate().toString());
        assertThat(userMonsterDto.getMonsterExpPoint()).isEqualTo(0L);
        assertThat(userMonsterDto.getIsFollowed()).isEqualTo(false);
        assertThat(userMonsterDto.getUser()).isEqualTo(testUser2);
    }

    @Test
    void searchTop10ByCategory(){

        List<User> userList = userRepository.searchTop10ByCategory(Category.Hobby);

        assertThat(userList.size()).isEqualTo(1L);
        assertThat(userList.get(0)).isEqualTo(testUser);
    }

    @Test
    void searchTop10ByAllCategories(){

        List<User> userList = userRepository.searchTop10ByCategory(null);

        assertThat(userList.size()).isEqualTo(2L);
        assertThat(userList.get(0)).isEqualTo(testUser2);
        assertThat(userList.get(1)).isEqualTo(testUser);
    }

    @Test
    void searchTop10ByFollow(){

        List<User> userList = userRepository.searchTop10ByFollow();

        assertThat(userList.size()).isEqualTo(1L);
        assertThat(userList.get(0)).isEqualTo(testUser);
    }











}