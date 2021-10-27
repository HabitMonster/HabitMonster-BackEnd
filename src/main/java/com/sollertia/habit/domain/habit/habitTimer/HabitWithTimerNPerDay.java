package com.sollertia.habit.domain.habit.habitTimer;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.Day;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Getter
@Entity
@DiscriminatorValue("N")
public class HabitWithTimerNPerDay extends HabitWithTimer {

    private Long sessionDuration;

    @Enumerated(EnumType.STRING)
    private Day day;

    private void setSessionDuration(Long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    private void setDay(Day day) {
        this.day = day;
    }

    public static HabitWithTimerNPerDay createHabitWithTimerNPerDay(HabitDtoImpl habitDtoImpl) {
        HabitWithTimerNPerDay habit = new HabitWithTimerNPerDay();
        LocalDate startDay = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        LocalDate endUpDate = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        habit.setGoalDurationTimePerSession(habit.getGoalDurationTimePerSession());
        habit.setTitle(habitDtoImpl.getTitle());
        habit.setDescription(habitDtoImpl.getDescription());
        habit.setDurationStart(startDay);
        habit.setDurationEnd(endUpDate);
        habit.setUser(habitDtoImpl.getUser());
        habit.setTeam(habitDtoImpl.getTeam());
        habit.setCategory(Category.fromString(habitDtoImpl.getCategory()));
        habit.setSessionDuration(habit.getSessionDuration());
        habit.setDay(Day.fromInt(startDay.getDayOfWeek().getValue()));
        return habit;
    }





}
