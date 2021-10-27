package com.sollertia.habit.domain.habit.habitCounter;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@DiscriminatorValue("S")
public class HabitWithCounterSpecificDay extends HabitWithCounter {

    private Boolean monday = false;
    private Boolean tuesday = false;
    private Boolean wednesday = false;
    private Boolean thursday = false;
    private Boolean friday = false;
    private Boolean saturday = false;
    private Boolean sunday = false;

    public static HabitWithCounterSpecificDay createHabitWithCounterSpecificDay(HabitDtoImpl habitDtoImpl) {
        HabitWithCounterSpecificDay habit = new HabitWithCounterSpecificDay();
        LocalDate startDay = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        LocalDate endUpDate = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        habit.setGoalCountInSession(habitDtoImpl.getCount());
        habit.setTitle(habitDtoImpl.getTitle());
        habit.setDescription(habitDtoImpl.getDescription());
        habit.setDurationStart(startDay);
        habit.setDurationEnd(endUpDate);
        habit.setUser(habitDtoImpl.getUser());
        habit.setTeam(habitDtoImpl.getTeam());
        habit.setCategory(Category.fromString(habitDtoImpl.getCategory()));
        return habit;
    }

}
