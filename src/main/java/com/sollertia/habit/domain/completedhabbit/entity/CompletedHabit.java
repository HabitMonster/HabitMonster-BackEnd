package com.sollertia.habit.domain.completedhabbit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
public class CompletedHabit extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private int accomplishCounter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private HabitType habitType;

    @Column(nullable = true)
    private Long goalTime;

    @Column(nullable = true)
    private Long goalCount;

    private Boolean isSuccess;

    private LocalDate startDate;

    private LocalDate endupDate;

    private Long achievementPercentage;

}
