package com.sollertia.habit.domain.history;

import com.sollertia.habit.domain.habit.habitCounter.HabitWithCounter;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class History {

    @Id
    @GeneratedValue
    private Long id;

    private LocalDateTime endUpDateTime;

    private Boolean isSuccessToday;

    private String habitTitle;

    private Category category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    public void setCategory(Category category) {
        this.category = category;
    }

    private void setEndUpDateTime(LocalDateTime endUpDateTime) {
        this.endUpDateTime = endUpDateTime;
    }

    private void setSuccessToday(Boolean successToday) {
        isSuccessToday = successToday;
    }

    private void setUser(User user) {
        this.user = user;
    }

    private void setHabitTitle(String habitTitle) {
        this.habitTitle = habitTitle;
    }

    protected History() {

    }

    public static History makeHistory(HabitWithCounter habit) {
        LocalDateTime schedulerNow = LocalDateTime.now().minusDays(1);
        History history = new History();
        history.setHabitTitle(habit.getTitle());
        history.setEndUpDateTime(schedulerNow);
        history.setUser(habit.getUser());
        history.setSuccessToday(habit.getIsAccomplishInSession());
        history.setCategory(habit.getCategory());
        return history;
    }


}
