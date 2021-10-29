package com.sollertia.habit.domain.habit;


import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.user.User;

import java.time.LocalDate;

public interface Habit {

    public Long getAchievePercentage();

    public Boolean check(Long Value);

    public String getTitle();

    public String getDescription();

    public Long getId();

    public LocalDate getDurationStart();

    public LocalDate getDurationEnd();

    public Category getCategory();

    public Long getCurrent();

    public Long getSessionDuration();

    public User getUser();

    public Boolean getIsAccomplishInSession();

    //public void plusCount();
}
