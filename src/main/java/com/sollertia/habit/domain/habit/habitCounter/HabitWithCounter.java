package com.sollertia.habit.domain.habit.habitCounter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.Habit;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
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

    private Long countPerSession = 0l;

    private Long goalCountInSession;

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

    public Long getGoalPercentage() {
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

    public static Habit createHabit(HabitDtoImpl habitDtoImpl) {
        Habit habit = null;
        switch (habitDtoImpl.getHabitSession()) {
            case NPERDAY:
                habit = HabitWithCounterNPerDay.createHabitWithCounterNPerDay(habitDtoImpl);
                break;
            case SPECIFICDAY:
                habit = HabitWithCounterSpecificDay.createHabitWithCounterSpecificDay(habitDtoImpl);
                break;
        }
        return habit;
    }
}
