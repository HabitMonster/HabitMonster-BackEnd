package com.sollertia.habit.testdata;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@AllArgsConstructor
@Builder
public class TestCompletedHabitDto {

    private String title;

    private int accomplishedSessionCounter;

    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private HabitType habitType;

    private Boolean isSuccess;

    private int achievement;
}

