package com.sollertia.habit.domain.completedhabbit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.utils.TimeStamped;
import com.sollertia.habit.testdata.TestCompletedHabitDto;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class CompletedHabit extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Long accomplishCounter;

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

    private void setTitle(String title) {
        this.title = title;
    }

    private void setAccomplishCounter(Long accomplishCounter) {
        this.accomplishCounter = accomplishCounter;
    }
    private void setUser(User user) {
        this.user = user;
    }

    private void setCategory(Category category) {
        this.category = category;
    }

    private void setHabitType(HabitType habitType) {
        this.habitType = habitType;
    }

    private void setGoalTime(Long goalTime) {
        this.goalTime = goalTime;
    }

    private void setGoalCount(Long goalCount) {
        this.goalCount = goalCount;
    }

    private void setSuccess(Boolean success) {
        this.isSuccess = success;
    }

    private void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    private void setEndupDate(LocalDate endupDate) {
        this.endupDate = endupDate;
    }

    private void setAchievementPercentage(Long achievementPercentage) {
        this.achievementPercentage = achievementPercentage;
    }

    public static CompletedHabit of(Habit habit) {
        CompletedHabit completedHabit = new CompletedHabit();
        completedHabit.setTitle(habit.getTitle());
        completedHabit.setAccomplishCounter(habit.getAccomplishCounter());
        completedHabit.setUser(habit.getUser());
        completedHabit.setCategory(habit.getCategory());
        completedHabit.setHabitType(HabitType.HABITWITHCOUNTER);
        completedHabit.setAchievementPercentage(habit.getAchievePercentage());
        completedHabit.setSuccess(completedHabit.getAchievementPercentage() >= 85L);
//        completedHabit.setGoalCount(habit.getCount());
//        completedHabit.setGoalTime();
        completedHabit.setStartDate(habit.getDurationStart());
        completedHabit.setEndupDate(habit.getDurationEnd());

        return completedHabit;
    }

    public static List<CompletedHabit> listOf(List<Habit> habitList) {
        List<CompletedHabit> completedHabitList = new ArrayList<>();
        for (Habit habit : habitList) {
            completedHabitList.add(CompletedHabit.of(habit));
        }
        return completedHabitList;
    }
    //test용
    public CompletedHabit(TestCompletedHabitDto dto){
        this.user = dto.getUser();
        this.title = dto.getTitle();
        this.isSuccess = dto.getIsSuccess();

    }
    //test용
    public CompletedHabit() {

    }

}
