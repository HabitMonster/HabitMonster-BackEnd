package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@DiscriminatorValue("C")
public class HabitWithCounter extends Habit {


    private long accomplishCounter = 0L;

    private Long goalCountInSession;

    @Override
    public Long getCurrent() {
        return this.accomplishCounter;
    }

    private void setGoalCountInSession(Long goalCountInSession) {
        this.goalCountInSession = goalCountInSession;
    }

    public static HabitWithCounter createHabitWithCounter(HabitDtoImpl habitDtoImpl) {
        HabitWithCounter habit = new HabitWithCounter();
        LocalDate startDay = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        LocalDate endUpDate = LocalDate.parse(habitDtoImpl.getDurationEnd(), DateTimeFormatter.ISO_DATE);
        habit.setTitle(habitDtoImpl.getTitle());
        habit.setDescription(habitDtoImpl.getDescription());
        habit.setDurationStart(startDay);
        habit.setDurationEnd(endUpDate);
        habit.setUser(habitDtoImpl.getUser());
        habit.setCategory(Category.fromString(habitDtoImpl.getCategory()));
        habit.setGoalCountInSession(habitDtoImpl.getCount());
        return habit;
    }

    @Override
    public Boolean check(Long value) {
        this.accomplishCounter += value;
        boolean isAccomplishToday = this.accomplishCounter >= this.goalCountInSession;
        if (isAccomplishToday) {
            this.setAccomplishInSession(true);
        }
        return isAccomplishToday;
    }

}
