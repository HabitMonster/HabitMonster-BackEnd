package com.sollertia.habit.domain.habit;

import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Habit {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private int accomplishSessionCounter = 0;

    private Boolean isAccomplishInSession;

    private Long expereincePoint;

    private LocalDateTime createdAt;

    private LocalDateTime endUpDateTime;

    @ManyToOne
    private User user;

    @ManyToOne
    private Team team;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Long sessionDuration;

    @Enumerated(EnumType.STRING)
    private Day day;

    public String getGoalPercentage() {
        // 달성률
        // return 달성한 날 수 / 총 기간(21일, 66일)	+ "%";
        return null;
    }

}
