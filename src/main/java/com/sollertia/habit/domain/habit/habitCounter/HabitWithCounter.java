package com.sollertia.habit.domain.habit.habitCounter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.HabitSession;
import com.sollertia.habit.domain.habit.habitTimer.HabitWithTimer;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class HabitWithCounter implements Habit {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private long accomplishSessionCounter = 0L;

    private Boolean isAccomplishInSession = false;

    private LocalDate durationStart;

    private LocalDate durationEnd;

    private Long current = 0l;

    private Long goalCountInSession;

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

    public Long getAchievePercentage() {
        return null;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    protected void setDurationStart(LocalDate durationStart) {
        this.durationStart = durationStart;
    }

    protected void setDurationEnd(LocalDate durationEnd) {
        this.durationEnd = durationEnd;
    }

    protected void setGoalCountInSession(Long goalCountInSession) {
        this.goalCountInSession = goalCountInSession;
    }

    protected void setSessionDuration(Long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }


    protected void setUser(User user) {
        this.user = user;
        user.getHabitsWithCounter().add(this);
    }

    protected void setTeam(Team team) {
        this.team = team;
        team.getHabitsWithCounter().add(this);
    }

    protected void setCategory(Category category) {
        this.category = category;
    }


    public static Habit createHabit(HabitSession habitSession, HabitDtoImpl habitDto) {
        switch (habitSession) {
            case NPERDAY:
                return HabitWithCounterNPerDay.createHabitWithCounterNPerDay(habitDto);
            case SPECIFICDAY:
                return HabitWithCounterSpecificDay.createHabitWithCounterSpecificDay(habitDto);
        }
        return null;
    }

    @Override
    public Boolean check(Long value) {
        this.current += value;
        boolean isAccomplishToday = this.current >= this.goalCountInSession;
        if (isAccomplishToday) {
            this.isAccomplishInSession = true;
        }
        return isAccomplishToday;
    }

}
