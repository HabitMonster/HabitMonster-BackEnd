package com.sollertia.habit.global.scheduler.service;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.preset.repository.PreSetRepository;
import com.sollertia.habit.domain.preset.service.PreSetService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.follow.repository.FollowRepository;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.RecommendationRepository;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.scheduler.repository.CategoryAvgRepository;
import com.sollertia.habit.global.utils.RandomUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.security.core.context.SecurityContext;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DataManagingSchedulerTest {

    @InjectMocks
    private DataManagingScheduler dataManagingScheduler;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RecommendationRepository recommendationRepository;
    @Mock
    private HabitRepository habitRepository;
    @Mock
    private CompletedHabitRepository completedHabitRepository;
    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private PreSetService preSetService;
    @Mock
    private PreSetRepository preSetRepository;
    @Mock
    private RandomUtil randomUtil;
    @Mock
    private FollowRepository followRepository;
    @Mock
    private CategoryAvgRepository categoryAvgRepository;

    User testUser;
    Monster monster;
    UserDetailsImpl mockUserDetails;
    LocalDate today;
    Habit habit;
    List<Habit> habits;
    HabitTypeDto habitTypeDto;

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
        today = LocalDate.now();

        HabitDtoImpl habitDto = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .count(10)
                .build();

        habitTypeDto = new HabitTypeDto("counter", "specificDay");
        habit = Habit.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
        Whitebox.setInternalState(habit, "id", 1l);
        habits = new ArrayList<>();
        habits.add(habit);

        habits = new ArrayList<>();
        habits.add(habit);
    }
        @Test
    void minusExpOnLapsedHabit() {
        //given
        String day = String.valueOf(today.getDayOfWeek().getValue());
        given(habitRepository.findHabitsWithDaysAndAccomplish(day, false))
                .willReturn(habits);
        given(monsterRepository.findByUserId(testUser.getId()))
                .willReturn(Optional.of(monster));

        //when
        dataManagingScheduler.minusExpOnLapsedHabit(today);

        //then
        verify(habitRepository).findHabitsWithDaysAndAccomplish(day, false);
        verify(habitRepository).updateAccomplishInSessionToFalse();
        verify(monsterRepository).findByUserId(testUser.getId());
    }

    @Test
    void expireHabit() {
        //given
        given(habitRepository.findAllByDurationEndLessThanEqual(today))
                .willReturn(habits);

        //when
        dataManagingScheduler.expireHabit(today);

        verify(habitRepository).findAllByDurationEndLessThanEqual(today);
        verify(completedHabitRepository).saveAll(any());
        verify(habitRepository).deleteAllById(any());
    }

    @Test
    void makeRecommendations() {
        //given
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        given(userRepository.searchTop10ByCategory(any(Category.class)))
                .willReturn(userList);
        given(userRepository.searchTop10ByCategory(null))
                .willReturn(userList);
        given(userRepository.searchTop10ByFollow())
                .willReturn(userList);

        //when
        dataManagingScheduler.makeRecommendations();

        verify(recommendationRepository).deleteAllInBatch();
        verify(recommendationRepository).saveAll(any());
        verify(userRepository, times(7)).searchTop10ByCategory(any(Category.class));
        verify(userRepository).searchTop10ByCategory(null);
        verify(userRepository).searchTop10ByFollow();
    }

    @Test
    void makePreset() {
    }

    @Test
    void dropUserIfDisabled() {
    }
}