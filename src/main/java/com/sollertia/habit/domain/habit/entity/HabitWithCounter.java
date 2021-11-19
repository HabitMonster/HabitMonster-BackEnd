package com.sollertia.habit.domain.habit.entity;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitUpdateRequestDto;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.habit.AlreadyGoalCountException;
import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@DiscriminatorValue("C")
public class HabitWithCounter extends Habit {


    private int todayCounter = 0;

    private int goalCountInSession;

    @Override
    public int getCurrent() {
        return this.todayCounter;
    }

    private void setCurrent(int todayCounter) {
        this.todayCounter = todayCounter;
    }

    @Override
    public void updateHabit(HabitUpdateRequestDto habitUpdateRequestDto) {
        this.updateTitle(habitUpdateRequestDto.getTitle());
        this.updateDescription(habitUpdateRequestDto.getDescription());
        this.goalCountInSession = habitUpdateRequestDto.getCount();
        if ( this.getCurrent() >= this.getGoalCountInSession() ) {
            this.setCurrent(this.getGoalCountInSession());
            this.accomplishToday();
        } else if ( this.getIsAccomplishInSession() ) {
            this.setAccomplishInSession(false);
            super.cancelAccomplishCounter();
        }
    }

    private void setGoalCountInSession(int goalCountInSession) {
        this.goalCountInSession = goalCountInSession;
    }

    public static HabitWithCounter createHabitWithCounter(HabitDtoImpl habitDtoImpl, User user) {
        HabitWithCounter habit = new HabitWithCounter();
        LocalDate startDay = LocalDate.parse(habitDtoImpl.getDurationStart(), DateTimeFormatter.ISO_DATE);
        LocalDate endUpDate = LocalDate.parse(habitDtoImpl.getDurationEnd(), DateTimeFormatter.ISO_DATE);
        habit.setTitle(habitDtoImpl.getTitle());
        habit.setDescription(habitDtoImpl.getDescription());
        habit.setDurationStart(startDay);
        habit.setDurationEnd(endUpDate);
        habit.setUser(user);
        habit.setCategory(Category.getCategory(habitDtoImpl.getCategoryId()));
        habit.setGoalCountInSession(habitDtoImpl.getCount());
        habit.setPracticeDays(habitDtoImpl.getPracticeDays());
        habit.setWholeDays();
        return habit;
    }

    @Override
    public Boolean check(Long value) {
        if ( this.todayCounter + value < this.goalCountInSession ) {
            this.todayCounter += value;
            return false;
        } else if ( this.todayCounter + value == this.goalCountInSession ) {
            this.todayCounter += value;
            this.accomplishToday();
            return true;
        } else {
            throw new AlreadyGoalCountException("Habit is completed today already");
        }
    }

    private void accomplishToday() {
        this.setAccomplishInSession(true);
        super.checkAccomplishCounter();
    }
}
