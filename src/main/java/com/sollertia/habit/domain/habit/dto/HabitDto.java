package com.sollertia.habit.domain.habit.dto;

// habit 과 habitDtoImpl의 연관관계가 너무 복잡한 것 같다.
// 가능하다면 인터페스나 추상 클래스로 조금 더 Dto를 추상화 해두고 싶다.
// 아직 미정

import com.sollertia.habit.domain.habit.enums.HabitSession;
import com.sollertia.habit.domain.habit.enums.HabitType;
import com.sollertia.habit.domain.team.Team;
import com.sollertia.habit.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class HabitDto {
    private HabitType habitType;
    private HabitSession habitSession;
    private User user;
    private Team team;
    private String title;
    private String description;
    private LocalDate durationStart;
    private LocalDate durationEnd;

    public void setHabitType(String habitType) {
        this.habitType = HabitType.fromString(habitType);
    }
    public void setHabitSession(String habitSession) {
        this.habitSession = HabitSession.fromString(habitSession);
    }
    public void setDurationStart(String durationStart) {
        LocalDate start = LocalDate.parse(durationStart, DateTimeFormatter.ISO_DATE);
        this.durationStart = start;

    }
    public void setDurationEnd(String durationEnd) {
        LocalDate end = LocalDate.parse(durationEnd, DateTimeFormatter.ISO_DATE);
        this.durationEnd = end;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

}
