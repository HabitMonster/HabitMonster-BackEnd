package com.sollertia.habit.domain.history.entity;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static History makeHistory(Habit habit) {
        LocalDateTime schedulerNow = LocalDateTime.now().minusDays(1);
        History history = new History();

        return history;
    }


}
