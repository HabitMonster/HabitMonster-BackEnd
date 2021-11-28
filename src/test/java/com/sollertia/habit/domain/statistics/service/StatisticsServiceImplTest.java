package com.sollertia.habit.domain.statistics.service;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsDto;
import com.sollertia.habit.domain.statistics.dto.GlobalStatisticsResponseDto;
import com.sollertia.habit.domain.statistics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.statistics.entity.Statistics;
import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.domain.statistics.repository.StatisticsRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class StatisticsServiceImplTest {

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Mock
    private CompletedHabitRepository completedHabitRepository;
    @Mock
    private StatisticsRepository statisticsRepository;
    @Mock
    private RandomUtil randomUtil;

    User testUser;
    HabitDtoImpl habitDto;
    List<CompletedHabit> completedHabitList = new ArrayList<>();

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);

        habitDto = HabitDtoImpl.builder()
                .durationStart("2021-11-01").durationEnd("2021-11-30")
                .count(3).title("title").description("description").practiceDays("1234567").categoryId(1L).build();
        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
        Habit habit = Habit.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
        CompletedHabit completedHabit1 = CompletedHabit.of(habit);
        CompletedHabit completedHabit2 = CompletedHabit.of(habit);
        Whitebox.setInternalState(completedHabit1, "isSuccess", true);
        Whitebox.setInternalState(completedHabit2, "isSuccess", false);
        completedHabitList.add(completedHabit1);
        completedHabitList.add(completedHabit2);
    }

    @Test
    void getStatistics() {
        //given
        String date = "2021-11";
        given(completedHabitRepository.
                findAllByUserAndStartDateBetweenOrderByStartDate(testUser,
                        LocalDate.of(2021, 11, 1),
                        LocalDate.of(2021, 11, 30)))
                .willReturn(completedHabitList);

        //when
        StatisticsResponseDto responseDto = statisticsService.getStatistics(testUser, date);

        //then
        assertThat(responseDto.getResponseMessage()).isEqualTo("Statistics Query Completed");
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getTotalCount()).isEqualTo(completedHabitList.size());
        assertThat(responseDto.getFailedCount()).isEqualTo(1);
        assertThat(responseDto.getSucceededCount()).isEqualTo(1);
        assertThat(responseDto.getHabitList().get(0).getTitle())
                .isEqualTo(completedHabitList.get(0).getTitle());
        assertThat(responseDto.getHabitList().get(0).getGoalCount())
                .isEqualTo(completedHabitList.get(0).getGoalCount());
        assertThat(responseDto.getHabitList().get(0).getAccomplishCount())
                .isEqualTo(completedHabitList.get(0).getAccomplishCounter());
        assertThat(responseDto.getHabitList().get(0).getEndUPDate())
                .isEqualTo(completedHabitList.get(0).getEndupDate().toString());
    }

    @Test
    void getGlobalStatistics() {
        //given
        Statistics statistics1 = new Statistics();
        Whitebox.setInternalState(statistics1, "id", 1L);
        Whitebox.setInternalState(statistics1, "contents", "test1");
        Whitebox.setInternalState(statistics1, "value", "test1");
        Whitebox.setInternalState(statistics1, "sessionType", SessionType.STATIC);
        Statistics statistics2 = new Statistics();
        Whitebox.setInternalState(statistics2, "id", 2L);
        Whitebox.setInternalState(statistics2, "contents", "test2");
        Whitebox.setInternalState(statistics2, "value", "test2");
        Whitebox.setInternalState(statistics2, "sessionType", SessionType.STATIC);
        List<Statistics> statistics = List.of(statistics1, statistics2);

        given(statisticsRepository.findAll()).willReturn(statistics);
        given(randomUtil.getRandomNumbers(2)).willReturn(new int[]{0,1});

        //when
        GlobalStatisticsResponseDto responseDto = statisticsService.getGlobalStatistics();

        //then
        assertThat(responseDto.getResponseMessage()).isEqualTo("Global Statistics Query Completed");
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
    }

    @Test
    void emptyGlobalStatisticsDto() {
        //given
        List<Statistics> statistics = new ArrayList<>();

        given(statisticsRepository.findAll()).willReturn(statistics);

        //when
        GlobalStatisticsResponseDto responseDto = statisticsService.getGlobalStatistics();

        //then
        assertThat(responseDto.getResponseMessage()).isEqualTo("Global Statistics is Empty");
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
    }
}