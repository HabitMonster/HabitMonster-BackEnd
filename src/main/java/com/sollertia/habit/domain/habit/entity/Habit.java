package com.sollertia.habit.domain.habit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitUpdateRequestDto;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.stream.Stream;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Habit extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Boolean isAccomplishInSession = false;

    private LocalDate durationStart;

    private LocalDate durationEnd;

    private String practiceDays;

    private Long accomplishCounter = 0l;

    // == n일에 n번 수행하는 습관 생성용 칼럼 ==
    private Long sessionDuration;

    private Long nPerDay;

    private LocalDate nextPracticeDay;

    private Long wholeDays;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team_id")
//    @JsonIgnore
//    private Team team;

    @Enumerated(EnumType.STRING)
    private Category category;

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

    public abstract int getCurrent();

    public void setWholeDays() {

        int wholeCount = 0;
        Long until = this.durationStart.until(durationEnd, ChronoUnit.DAYS);
        int wholeDays = until.intValue();

        int startDay = this.durationStart.getDayOfWeek().getValue();

        ArrayList<Integer> days = new ArrayList<>();
        int[] ints = Stream.of(this.practiceDays.split("")).mapToInt(Integer::parseInt).toArray();
        for (int anInt : ints) {
            days.add(anInt);
        }

        if (days.stream().anyMatch(x -> x == 7)) {
            days.add(0);
        }

        if (this.practiceDays.length() == 7) {
            this.wholeDays = Long.valueOf(wholeDays);
            return;
        }


        wholeCount += (wholeDays / 7) * this.practiceDays.length();

        if (wholeDays % 7 == 0) {
            this.wholeDays = Long.valueOf(wholeCount);
            return;
        } else {
            for (int i = startDay; i <= startDay + (wholeDays % 7); i++) {
                int leftover = i % 7;
                boolean contains = days.stream().anyMatch(x -> x == leftover);
                if (contains) {
                    wholeCount += 1;
                }
            }
            this.wholeDays = Long.valueOf(wholeCount);
            return;
        }

    }

    public Long getAchievePercentage() {
        if (wholeDays == 0) {
            return 0l;
        }
        double percentage = ((double)this.accomplishCounter / (double)this.wholeDays) * 100;
        return Math.round(percentage);
    }
    protected void checkAccomplishCounter() {
        this.accomplishCounter += 1;
    }
    protected void cancelAccomplishCounter() {
        this.accomplishCounter -= 1;
    }

    protected void setUser(User user) {
        this.user = user;
        user.getHabit().add(this);
    }

//    protected void setTeam(Team team) {
//        this.team = team;
//        team.getHabitsWithCounter().add(this);
//    }

    protected void setCategory(Category category) {
        this.category = category;
    }


    public static Habit createHabit(HabitType habitType, HabitDtoImpl habitDto, User user) {
        switch (habitType) {
            case HABITWITHTIMER:
                return HabitWithTimer.createHabitWithTimer(habitDto, user);
            case HABITWITHCOUNTER:
                return HabitWithCounter.createHabitWithCounter(habitDto, user);
        }
        return null;
    }

    public abstract void updateHabit(HabitUpdateRequestDto habitUpdateRequestDto);

    protected void updateTitle(String title) {
        this.title = title;
    }

    protected void updateDescription(String description) {
        this.description = description;
    }
    public abstract Boolean check(Long value);



}
