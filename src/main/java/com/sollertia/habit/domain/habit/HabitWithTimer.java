package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import lombok.Getter;
import com.sollertia.habit.domain.user.User;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@DiscriminatorValue("T")
@NoArgsConstructor
public class HabitWithTimer extends Habit {


    private int currentDuration = 0;

    private int goalDurationTime;

    @Override
    public int getCurrent() {
        return this.currentDuration;
    }

    public void setGoalDurationTime(int goalDurationTime) {
        this.goalDurationTime = goalDurationTime;
    }

    public static HabitWithTimer createHabitWithTimer(HabitDtoImpl habitDtoImpl, User user) {
        HabitWithTimer habit = new HabitWithTimer();
        LocalDate startDay = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        LocalDate endUpDate = LocalDate.parse(habitDtoImpl.getDurationEnd(), DateTimeFormatter.ISO_DATE);
        habit.setTitle(habitDtoImpl.getTitle());
        habit.setDescription(habitDtoImpl.getDescription());
        habit.setDurationStart(startDay);
        habit.setDurationEnd(endUpDate);
        habit.setUser(user);
        habit.setCategory(Category.getCategory(habitDtoImpl.getCategoryId()));
        habit.setGoalDurationTime(habitDtoImpl.getCount());
        return habit;
    }

    @Override
    public Boolean check(Long value) {
        this.currentDuration += value;
        boolean isAccomplishToday = this.currentDuration >= this.goalDurationTime;
        if (isAccomplishToday) {
            this.setAccomplishInSession(true);
        }
        return isAccomplishToday;
    }

}
