package com.sollertia.habit.domain.habit.service;

import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.habit.dto.HabitCheckResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDetailResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.entity.HabitWithTimer;
import com.sollertia.habit.domain.habit.repository.HabitRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithCounterRepository;
import com.sollertia.habit.domain.habit.repository.HabitWithTimerRepository;
import com.sollertia.habit.domain.history.entity.History;
import com.sollertia.habit.domain.history.repository.HistoryRepository;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.doNothing;

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
    private HabitWithTimerRepository habitWithTimerRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private CompletedHabitRepository completedHabitRepository;

    @Mock
    private MonsterService monsterService;

    User testUser;

    HabitTypeDto habitTypeDto1;
    HabitTypeDto habitTypeDto2;

    HabitDtoImpl habitDto;

    HabitWithCounter habit1;
    HabitWithTimer habit2;

    LocalDate today;


    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);

        habitTypeDto1 = new HabitTypeDto("counter", "specificDay");
        habitTypeDto2 = new HabitTypeDto("timer", "specificDay");

        today = LocalDate.now();

        habitDto = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .count(10)
                .build();

        habit1 = (HabitWithCounter) Habit.createHabit(habitTypeDto1.getHabitType(), habitDto, testUser);
        habit2 = (HabitWithTimer) Habit.createHabit(habitTypeDto2.getHabitType(), habitDto, testUser);
    }

    @Test
    public void createHabitAboutTypeTest() throws Exception {
        //given
        given(habitRepository.save(any())).willReturn(habit1);

        //when
        HabitDetailResponseDto result = habitService.createHabit(habitTypeDto1, habitDto, testUser);

        //then
        assertThat(result.getHabit().getHabitId()).isEqualTo(habit1.getId());
        assertThat(result.getHabit().getCategory()).isEqualTo(habit1.getCategory());
        assertThat(result.getHabit().getCount()).isEqualTo(habit1.getGoalInSession());
        assertThat(result.getHabit().getTotalCount()).isEqualTo(habit1.getTotalCount());
        assertThat(result.getHabit().getDescription()).isEqualTo(habit1.getDescription());
        assertThat(result.getHabit().getDurationEnd()).isEqualTo(habit1.getDurationEnd().toString());
        assertThat(result.getHabit().getDurationStart()).isEqualTo(habit1.getDurationStart().toString());
        assertThat(result.getHabit().getAchievePercentage()).isEqualTo(habit1.getAchievePercentage());
        assertThat(result.getHabit().getPracticeDays()).isEqualTo(habit1.getPracticeDays());
        assertThat(result.getHabit().getCurrent()).isEqualTo(habit1.getCurrent());
        assertThat(result.getHabit().getCategoryId()).isEqualTo(habit1.getCategory().getCategoryId());
        assertThat(result.getHabit().getTitle()).isEqualTo(habit1.getTitle());
        assertThat(result.getHabit().getIsAccomplished()).isEqualTo(habit1.getIsAccomplishInSession());

        assertThat(result.getResponseMessage()).isEqualTo("Habit registered Completed");
        assertThat(result.getStatusCode()).isEqualTo(200);

    }

    @Test
    public void getHabitDetailTest() throws Exception {
        //given
        given(habitWithCounterRepository.findById(any())).willReturn(Optional.ofNullable(habit1));
        //when
        HabitDetailResponseDto result = habitService.getHabitDetail(habitTypeDto1, 1l);
        //then
        assertThat(result.getHabit().getHabitId()).isEqualTo(habit1.getId());
        assertThat(result.getHabit().getCategory()).isEqualTo(habit1.getCategory());
        assertThat(result.getHabit().getCount()).isEqualTo(habit1.getGoalInSession());
        assertThat(result.getHabit().getTotalCount()).isEqualTo(habit1.getTotalCount());
        assertThat(result.getHabit().getDescription()).isEqualTo(habit1.getDescription());
        assertThat(result.getHabit().getDurationEnd()).isEqualTo(habit1.getDurationEnd().toString());
        assertThat(result.getHabit().getDurationStart()).isEqualTo(habit1.getDurationStart().toString());
        assertThat(result.getHabit().getAchievePercentage()).isEqualTo(habit1.getAchievePercentage());
        assertThat(result.getHabit().getPracticeDays()).isEqualTo(habit1.getPracticeDays());
        assertThat(result.getHabit().getCurrent()).isEqualTo(habit1.getCurrent());
        assertThat(result.getHabit().getCategoryId()).isEqualTo(habit1.getCategory().getCategoryId());
        assertThat(result.getHabit().getTitle()).isEqualTo(habit1.getTitle());
        assertThat(result.getHabit().getIsAccomplished()).isEqualTo(habit1.getIsAccomplishInSession());

        assertThat(result.getResponseMessage()).isEqualTo("Habit Detail Query Completed");
        assertThat(result.getStatusCode()).isEqualTo(200);
    }


    @Test
    public void checkHabitTest() throws Exception {
        //given
        HabitDtoImpl habitDtoCheck = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .count(1)
                .build();

        HabitDtoImpl habitDtoDueToday = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd(today.toString())
                .categoryId(2l)
                .practiceDays("1234567")
                .count(1)
                .build();


        HabitWithCounter habitCheck = (HabitWithCounter) Habit.createHabit(habitTypeDto1.getHabitType(), habitDtoCheck, testUser);
        HabitWithCounter habitDueToday = (HabitWithCounter) Habit.createHabit(habitTypeDto1.getHabitType(), habitDtoDueToday, testUser);
        //habit id 1 -> isAchieve = false
        given(habitWithCounterRepository.findById(1l)).willReturn(Optional.ofNullable(habit1));
        //habit id 2 -> isAchieve = true
        given(habitWithCounterRepository.findById(2l)).willReturn(Optional.ofNullable(habitCheck));
        //habit id 3 -> isCompleteToday = true
        given(habitWithCounterRepository.findById(3l)).willReturn(Optional.ofNullable(habitDueToday));

        given(habitWithCounterRepository.save(habit1)).willReturn(habit1);

        given(habitWithCounterRepository.save(habitCheck)).willReturn(habitCheck);

        given(habitWithCounterRepository.save(habitDueToday)).willReturn(habitDueToday);


        //when
        HabitCheckResponseDto habitCheckResponseDto = habitService.checkHabit(habitTypeDto1, 1l, today);

        HabitCheckResponseDto habitCheckResponseDto2 = habitService.checkHabit(habitTypeDto1, 2l, today);

        HabitCheckResponseDto habitCheckResponseDto3 = habitService.checkHabit(habitTypeDto1, 3l, today);



        //then


        assertThat(habitCheckResponseDto.getStatusCode()).isEqualTo(200);
        assertThat(habitCheckResponseDto.getResponseMessage()).isEqualTo("Check Habit Completed");
        assertThat(habitCheckResponseDto2.getStatusCode()).isEqualTo(200);
        assertThat(habitCheckResponseDto2.getResponseMessage()).isEqualTo("Check Habit Completed");
        assertThat(habitCheckResponseDto3.getStatusCode()).isEqualTo(200);
        assertThat(habitCheckResponseDto3.getResponseMessage()).isEqualTo("Check Habit Completed");

        verify(monsterService, times(2)).plusExpPoint(testUser);
        verify(historyRepository, times(2)).save(any());
        verify(completedHabitRepository, times(1)).save(any());
        verify(habitWithCounterRepository, times(1)).delete(habitDueToday);
    }



}
