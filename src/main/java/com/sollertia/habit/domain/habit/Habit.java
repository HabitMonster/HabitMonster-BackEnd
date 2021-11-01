package com.sollertia.habit.domain.habit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Stream;

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

    private String practiceDays;

    private int accomplishCounter = 0;

    // == n일에 n번 수행하는 습관 생성용 칼럼 ==
    private Long sessionDuration;

    private Long nPerDay;

    private LocalDate nextPracticeDay;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;

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

    public Long getAchievePercentage() {

        // 분모
        int wholeCount = 0;
        // 총 기간 int값
        int wholeDays = Long.valueOf(Duration.between(this.durationStart.atStartOfDay(), this.durationEnd.atStartOfDay()).toDays()).intValue();
        // 시작일 int값(1~7)(월~일)
        int startDay = this.durationStart.getDayOfWeek().getValue();
        // 수행하기로 한 요일 배열
        ArrayList<Integer> days = new ArrayList<>();
        int[] ints = Stream.of(this.practiceDays.split("")).mapToInt(Integer::parseInt).toArray();
        for (int anInt : ints) {
            days.add(anInt);
        }
        // 7로 나눈 나머지와 값 비교를 하기 위해 7이 있다면 0을 넣어줌
        if (days.stream().anyMatch(x -> x == 7)) {
            days.add(0);
        }
        // 매일하는 습관인 경우
        if (this.practiceDays.length() == 7) {
            // 총 수행한 날(= accomplishCounter) / 총 기간 -> 달성율
            return this.accomplishCounter / Long.valueOf(wholeDays);
        }

        // 매일하는 습관이 아닌경우 분모값 += (매 주 수행해야하는 요일 수 * 주(week) 수)
        wholeCount += (wholeDays / 7) * this.practiceDays.length();

        // 만약 총 기간이 week로 나누어 떨어진다면
        if (wholeDays % 7 == 0) {
            // whole counter == 총 수행일
            return this.accomplishCounter / Long.valueOf(wholeCount);
        } else {
            // 나머지가 있다면 (시작요일 + 나머지)를 7로 나누어 해당 요일에 습관을 수행하기로 했는지 비교
            for (int i = startDay; i <= startDay + (wholeDays % 7); i++) {
                int i1 = i % 7;
                boolean contains = days.stream().anyMatch(x -> x == i1);
                // 해당 요일 있으면 값추가
                if (contains) {
                    wholeDays += 1;
                }
            }
            return this.accomplishCounter / Long.valueOf(wholeDays);
        }

    }

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


    public static Habit createHabit(HabitType habitType, HabitDtoImpl habitDto, User user) {
        switch (habitType) {
            case HABITWITHTIMER:
                return HabitWithTimer.createHabitWithTimer(habitDto, user);
            case HABITWITHCOUNTER:
                return HabitWithCounter.createHabitWithCounter(habitDto, user);
        }
        return null;
    }


    public abstract Boolean check(Long value);



}
