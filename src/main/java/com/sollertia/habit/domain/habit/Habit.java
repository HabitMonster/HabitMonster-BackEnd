package com.sollertia.habit.domain.habit;


import com.sollertia.habit.domain.habit.enums.Category;

import java.time.LocalDate;

public interface Habit {

    public Long getAchievePercentage();

    public String getTitle();

    public String getDescription();

    public Long getId();

    public LocalDate getDurationStart();

    public LocalDate getDurationEnd();

    public Category getCategory();

    public Long getCurrent();

    public Long getSessionDuration();

    //public void plusCount();
}
