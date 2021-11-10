package com.sollertia.habit.domain.completedhabbit.statics.service;

import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.repository.CompletedHabitRepository;
import com.sollertia.habit.domain.completedhabbit.statics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
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

        Calendar startDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");

        habitDto = HabitDtoImpl.builder()
                .durationStart(form.format(startDate.getTime())).durationEnd(form.format(startDate.getTime()))
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
}