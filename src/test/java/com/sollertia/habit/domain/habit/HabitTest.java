package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.HabitType;


import com.sollertia.habit.domain.user.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HabitTest {

    @Test
    public void createHabitTest() {

        //given
        User testUser = mock(User.class);
        when(testUser.getHabit()).thenReturn(new ArrayList<Habit>());
        HabitDtoImpl habitDto = createHabitDto("2021-11-17", "2022-11-17", "345");

        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHTIMER, habitDto, testUser);

        //then
        Assertions.assertEquals(habit1.getClass(), HabitWithCounter.class);
        Assertions.assertEquals(habit2.getClass(), HabitWithTimer.class);

    }

    @Test
    public void getWholeDayTest() {

        //given
        User testUser = mock(User.class);
        when(testUser.getHabit()).thenReturn(new ArrayList<Habit>());
        HabitDtoImpl habitDto1 = createHabitDto("2021-11-17", "2022-11-17", "1234567"); //매일하는 습관, 1년, 365회
        HabitDtoImpl habitDto2 = createHabitDto("2021-11-03", "2021-11-24", "156"); //3주, 주당 3회, 9회
        HabitDtoImpl habitDto3 = createHabitDto("2021-11-03", "2021-11-30", "127"); //12번, 일요일 건너감

        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto1, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto2, testUser);
        Habit habit3 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto3, testUser);

        //then
        Assertions.assertEquals(365l, habit1.getWholeDays());
        Assertions.assertEquals(9l, habit2.getWholeDays());
        Assertions.assertEquals(12l, habit3.getWholeDays());

    }

    private HabitDtoImpl createHabitDto(String start, String end, String days) {
        return HabitDtoImpl.builder()
                .title("TestTitle")
                .description("TestDescription")
                .count(3)
                .durationStart(start)
                .durationEnd(end)
                .categoryId(2l)
                .practiceDays(days)
                .build();
    }


}