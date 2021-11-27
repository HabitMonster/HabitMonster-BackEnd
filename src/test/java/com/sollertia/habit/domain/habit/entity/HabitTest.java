package com.sollertia.habit.domain.habit.entity;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.HabitType;


import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.habit.BadDataAboutHabitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitTest {

    User testUser;

    @BeforeEach
    public void beforeEach() {
        testUser = mock(User.class);
        when(testUser.getHabit()).thenReturn(new ArrayList<Habit>());
    }

    @Test
    public void createHabitTest() {

        //given
        HabitDtoImpl habitDto = createHabitDto("2021-11-17", "2022-11-17", "345");

        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHTIMER, habitDto, testUser);

        //then


        assertThat(habit1.getClass()).isEqualTo(HabitWithCounter.class);
        assertThat(habit2.getClass()).isEqualTo(HabitWithTimer.class);

    }

    @Test
    public void getWholeDayTest() {

        //given
        HabitDtoImpl habitDto1 = createHabitDto("2021-11-17", "2022-11-17", "1234567"); //매일하는 습관, 1년, 365회
        HabitDtoImpl habitDto2 = createHabitDto("2021-11-03", "2021-11-24", "156"); //3주, 주당 3회, 9회
        HabitDtoImpl habitDto3 = createHabitDto("2021-11-03", "2021-11-30", "127"); //12번, 일요일 건너감

        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto1, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto2, testUser);
        Habit habit3 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto3, testUser);

        //then

        assertThat(habit1.getWholeDays()).isEqualTo(365l);
        assertThat(habit2.getWholeDays()).isEqualTo(9l);
        assertThat(habit3.getWholeDays()).isEqualTo(12l);

    }

    @Test
    public void getAchievePercentageTest() throws Exception {

        //given
        HabitDtoImpl habitDto2 = createHabitDto("2021-11-17", "2021-11-27", "1234567");
        HabitDtoImpl habitDto3 = createHabitDto("2021-11-17", "2021-11-30", "1234567");


        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto2, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto3, testUser);
        //when

        habit1.check(1l);
        habit2.check(1l);

        //then
        assertThat(habit1.getAchievePercentage()).isEqualTo(10l);
        assertThat(habit2.getAchievePercentage()).isEqualTo(8l);
    }


    @Test
    public void setWholeDays_failedCase() throws Exception{

        //given
        HabitDtoImpl habitDto1 = createHabitDto("2021-11-17", "2021-11-17", "1234567");


        //when
        BadDataAboutHabitException thrown = Assertions
                .assertThrows(BadDataAboutHabitException.class, () -> Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto1, testUser));

        //then
        assertThat(thrown.getMessage()).isEqualTo("Bad Habit Data About Date");

    }


    private HabitDtoImpl createHabitDto(String start, String end, String days) {
        return HabitDtoImpl.builder()
                .title("TestTitle")
                .description("TestDescription")
                .count(1)
                .durationStart(start)
                .durationEnd(end)
                .categoryId(2l)
                .practiceDays(days)
                .build();
    }


}