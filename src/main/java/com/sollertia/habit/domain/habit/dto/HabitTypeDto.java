package com.sollertia.habit.domain.habit.dto;

import com.sollertia.habit.domain.habit.enums.HabitSession;
import com.sollertia.habit.domain.habit.enums.HabitType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitTypeDto {

    private HabitType habitType;
    private HabitSession habitSession;

    public HabitTypeDto(String type, String session) {
        this.habitType = HabitType.fromString(type);
        this.habitSession = HabitSession.fromString(session);
    }

}
