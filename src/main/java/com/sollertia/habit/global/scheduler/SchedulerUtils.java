package com.sollertia.habit.global.scheduler;

import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SchedulerUtils {

    public static SearchDateDto getSearchDateDto(SessionType durationEnum) {
        LocalDate schedulerNow = LocalDate.now();
        SearchDateDto result = null;
        switch (durationEnum) {
            case DAILY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusDays(1),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

            case WEEKLY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusDays(8),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

            case MONTHLY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusMonths(1),
                        LocalDateTime.of(schedulerNow.minusDays(1), LocalTime.MAX).withNano(0)
                );
                break;

        }
        return result;
    }

}
