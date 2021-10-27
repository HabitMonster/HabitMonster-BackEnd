package com.sollertia.habit.domain.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.Day;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Habit {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private long accomplishSessionCounter = 0L;

    private Boolean isAccomplishInSession;

    private LocalDateTime durationStart;

    private LocalDateTime durationEnd;

    private Long sessionDuration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

    @Enumerated(EnumType.STRING)
    private Category category;


    @Enumerated(EnumType.STRING)
    private Day day;

    public Long getGoalPercentage() {
        long between = ChronoUnit.DAYS.between(durationStart, durationEnd);
        if (this.sessionDuration == 1) {
            return this.accomplishSessionCounter / between;
        } else {
            return this.accomplishSessionCounter / (between / 7);
        }
    }

}
