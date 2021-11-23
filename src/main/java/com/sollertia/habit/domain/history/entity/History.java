package com.sollertia.habit.domain.history.entity;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime endUpDateTime;

    private Boolean isSuccessToday;

    private String habitTitle;

    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
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

    public static History makeHistory(Habit habit) {
        LocalDateTime schedulerNow = LocalDateTime.now();
        History history = new History();
        history.setCategory(habit.getCategory());
        history.setSuccessToday(habit.getIsAccomplishInSession());
        history.setEndUpDateTime(history.getIsSuccessToday() ?
                schedulerNow : schedulerNow.minusDays(1));
        history.setHabitTitle(habit.getTitle());
        history.setUser(habit.getUser());
        return history;
    }


}
