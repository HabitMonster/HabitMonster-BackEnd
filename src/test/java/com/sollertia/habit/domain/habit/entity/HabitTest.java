package com.sollertia.habit.domain.habit.entity;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitUpdateRequestDto;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.habit.BadDataAboutHabitException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("HabitWithCounter 인 경우")
    public void createHabitTest1() {
        //given
        HabitDtoImpl habitDto = createHabitDto("2021-11-17", "2022-11-17", "345");
        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto, testUser);
        //then
        assertThat(habit1.getClass()).isEqualTo(HabitWithCounter.class);


    }

    @Test
    @DisplayName("HabitWithTimer 인 경우")
    public void createHabitTest2() throws Exception {
        //given
        HabitDtoImpl habitDto = createHabitDto("2021-11-17", "2022-11-17", "345");
        //when
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHTIMER, habitDto, testUser);
        //then
        assertThat(habit2.getClass()).isEqualTo(HabitWithTimer.class);
    }

    @Test
    @DisplayName("Get Whole Days Case: 매일하는습관")
    public void getWholeDayTest1() {

        //given
        HabitDtoImpl habitDto1 = createHabitDto("2021-11-17", "2022-11-16", "1234567"); //매일하는 습관, 1년, 365회
        //when
        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto1, testUser);
        //then
        assertThat(habit1.getWholeDays()).isEqualTo(365l);

    }
    @Test
    @DisplayName("Get Whole Days Case: 정해진 요일에 하지만, 총 기간이 7로 나누어 떨어지는 습관")
    public void getWholeDayTest2() {

        //given
        HabitDtoImpl habitDto2 = createHabitDto("2021-11-03", "2021-11-24", "156"); //3주, 주당 3회, 9회
        //when
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto2, testUser);
        //then
        assertThat(habit2.getWholeDays()).isEqualTo(9l);

    }

    @Test
    @DisplayName("Get Whole Days Case: 정해진 요일에 수행하며, 나머지가 발생하는 습관")
    public void getWholeDayTest3() {

        //given
        HabitDtoImpl habitDto3 = createHabitDto("2021-11-03", "2021-11-30", "127"); //12번, 일요일 건너감

        //when
        Habit habit3 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto3, testUser);

        //then
        assertThat(habit3.getWholeDays()).isEqualTo(12l);

    }

    @Test
    @DisplayName("달성율 계산 확인 테스트")
    public void getAchievePercentageTest() throws Exception {

        //given
        HabitDtoImpl habitDto2 = createHabitDto("2021-11-17", "2021-11-26", "1234567");
        HabitDtoImpl habitDto3 = createHabitDto("2021-11-17", "2021-11-29", "1234567");


        Habit habit1 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto2, testUser);
        Habit habit2 = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto3, testUser);
        //when

        habit1.checkCount(1l);
        habit2.checkCount(1l);

        //then
        assertThat(habit1.getAchievePercentage()).isEqualTo(10l);
        assertThat(habit2.getAchievePercentage()).isEqualTo(8l);
    }


    @Test
    @DisplayName("총 기간 셋팅 시 계산된 총 기간이 일주일 미만인 경우 Throw Exception")
    public void setWholeDays_failedCase() throws Exception{

        //given
        HabitDtoImpl habitDto1 = createHabitDto("2021-11-17", "2021-11-17", "1234567");


        //when
        BadDataAboutHabitException thrown = Assertions
                .assertThrows(BadDataAboutHabitException.class, () -> Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto1, testUser));

        //then
        assertThat(thrown.getMessage()).isEqualTo("Bad Habit Data About Date");

    }

    @Test
    @DisplayName("Habit Update Basic Case: title, description")
    public void updateHabitTest_TitleDescription() throws Exception {
        //given
        HabitDtoImpl habitDto = createHabitDto("2021-11-17", "2022-11-17", "1234567");
        Habit habit = Habit.createHabit(HabitType.HABITWITHCOUNTER, habitDto, testUser);
        HabitUpdateRequestDto habitUpdateRequestDto = new HabitUpdateRequestDto("UpdateCompletedTitle", "Updated", 3);
        //when
        habit.updateHabit(habitUpdateRequestDto);
        //then
        assertThat(habit.getTitle()).isEqualTo("UpdateCompletedTitle");
        assertThat(habit.getDescription()).isEqualTo("Updated");

    }

    @Test
    @DisplayName("Habit Update Case: 목표 카운트가 줄어들어 습관 달성이 성공하는 경우")
    public void updateHabitTest_current_count_is_bigger_than_new_goal_count() throws Exception {
        //given
        HabitDtoImpl testDto = HabitDtoImpl.builder()
                .title("test")
                .description("test")
                .count(3)
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .build();
        Habit habit = Habit.createHabit(HabitType.HABITWITHCOUNTER, testDto, testUser);

        //when
        habit.checkCount(1l);
        habit.checkCount(1l); //current = 2, goal = 3

        HabitUpdateRequestDto habitUpdateRequestDto = new HabitUpdateRequestDto("UpdateCompletedTitle", "Updated", 2);
        habit.updateHabit(habitUpdateRequestDto); //change goal to 2

        //then
        assertThat(habit.getCurrent()).isEqualTo(2);
        assertThat(habit.getIsAccomplishInPeriod()).isTrue();
    }

    @Test
    @DisplayName("Habit Update Case: 목표 카운트가 늘어나 성공했던 습관 달성이 진행중으로 변경되는 경우")
    public void updateHabitTest_new_goal_count_is_bigger_than_current_one() throws Exception {
        //given
        HabitDtoImpl testDto = HabitDtoImpl.builder()
                .title("test")
                .description("test")
                .count(3)
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .build();
        Habit habit = Habit.createHabit(HabitType.HABITWITHCOUNTER, testDto, testUser);
        //when
        habit.checkCount(1l);
        habit.checkCount(1l);
        habit.checkCount(1l);

        HabitUpdateRequestDto habitUpdateRequestDto = new HabitUpdateRequestDto("UpdateCompletedTitle", "Updated", 5);
        habit.updateHabit(habitUpdateRequestDto);

        //then
        assertThat(habit.getIsAccomplishInPeriod()).isFalse();
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