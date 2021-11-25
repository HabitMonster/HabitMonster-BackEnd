package com.sollertia.habit.domain.habit.service;

import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitDetailResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.entity.HabitWithTimer;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@RunWith(PowerMockRunner.class)
@ExtendWith(MockitoExtension.class)
class HabitServiceImplTest {

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private HabitServiceImpl habitService;

    @Mock
    private HabitWithCounterRepository habitWithCounterRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private CompletedHabitRepository completedHabitRepository;

    @Mock
    private MonsterService monsterService;

    User testUser;

    HabitTypeDto habitTypeDto;

    HabitDtoImpl habitDto;

    Habit habit;


    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);

        habitTypeDto = new HabitTypeDto("counter", "specificDay");

        habitDto = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .count(10)
                .build();

        habit = HabitWithCounter.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
    }

    @Test
    public void createHabitAboutTypeTest() throws Exception {
        //given
        given(habitRepository.save(any())).willReturn(habit);

        //when
        HabitDetailResponseDto result = habitService.createHabit(habitTypeDto, habitDto, testUser);

        //then
        assertThat(result.getHabit().getHabitId()).isEqualTo(habit.getId());
        assertThat(result.getHabit().getCategory()).isEqualTo(habit.getCategory());
        assertThat(result.getHabit().getCount()).isEqualTo(habit.getGoalInSession());
        assertThat(result.getHabit().getTotalCount()).isEqualTo(habit.getTotalCount());
        assertThat(result.getHabit().getDescription()).isEqualTo(habit.getDescription());
        assertThat(result.getHabit().getDurationEnd()).isEqualTo(habit.getDurationEnd().toString());
        assertThat(result.getHabit().getDurationStart()).isEqualTo(habit.getDurationStart().toString());
        assertThat(result.getHabit().getAchievePercentage()).isEqualTo(habit.getAchievePercentage());
        assertThat(result.getHabit().getPracticeDays()).isEqualTo(habit.getPracticeDays());
        assertThat(result.getHabit().getCurrent()).isEqualTo(habit.getCurrent());
        assertThat(result.getHabit().getCategoryId()).isEqualTo(habit.getCategory().getCategoryId());
        assertThat(result.getHabit().getTitle()).isEqualTo(habit.getTitle());
        assertThat(result.getHabit().getIsAccomplished()).isEqualTo(habit.getIsAccomplishInSession());

        assertThat(result.getResponseMessage()).isEqualTo("Habit registered Completed");
        assertThat(result.getStatusCode()).isEqualTo(200);

    }



}
