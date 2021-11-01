package com.sollertia.habit.domain.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.Category;
import com.sollertia.habit.domain.habit.enums.HabitType;
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
public abstract class Habit {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private Boolean isAccomplishInSession = false;

    private LocalDate durationStart;

    private LocalDate durationEnd;

    private Long sessionDuration;

    private String practiceDays;

    // == n일에 n번 수행하는 습관 생성용 칼럼 ==

    private Long nPerDay;

    private LocalDate nextPracticeDay;


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

    protected void setSessionDuration(Long sessionDuration) {
        this.sessionDuration = sessionDuration;
    }

    protected void setPracticeDays(String practiceDays) {
        this.practiceDays = practiceDays;
    }

    protected void setNextPracticeDay(LocalDate nextPracticeDay) {
        this.nextPracticeDay = nextPracticeDay;
    }

    protected void setAccomplishInSession(Boolean accomplishInSession) {
        isAccomplishInSession = accomplishInSession;
    }

    public abstract Long getCurrent();

    protected void setUser(User user) {
        this.user = user;
        user.getHabit().add(this);
    }

    protected void setTeam(Team team) {
        this.team = team;
        team.getHabitsWithCounter().add(this);
    }

    protected void setCategory(Category category) {
        this.category = category;
    }


    public static Habit createHabit(HabitType habitType, HabitDtoImpl habitDto) {
        switch (habitType) {
            case HABITWITHTIMER:
                return HabitWithTimer.createHabitWithTimer(habitDto);
            case HABITWITHCOUNTER:
                return HabitWithCounter.createHabitWithCounter(habitDto);
        }
        return null;
    }


    public abstract Boolean check(Long value);



}
