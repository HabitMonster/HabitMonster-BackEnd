package com.sollertia.habit.global.scheduler;

import com.sollertia.habit.domain.statistics.enums.SessionType;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class SchedulerUtils {

    public static SearchDateDto getSearchDateDto(SessionType sessionType) {
        LocalDate schedulerNow = LocalDate.now();
        SearchDateDto result = null;
        switch (sessionType) {
            case DAILY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay(),
                        LocalDateTime.of(schedulerNow, LocalTime.MAX).withNano(0)
                );
                break;

            case WEEKLY:
                result = new SearchDateDto(
                        schedulerNow.atStartOfDay().minusDays(7),
                        LocalDateTime.of(schedulerNow, LocalTime.MAX).withNano(0)
                );
                break;

            case MONTHLY:
                LocalDate lastMonth = schedulerNow.minusMonths(1L);
                result = new SearchDateDto(
                        LocalDateTime.of(lastMonth.withDayOfMonth(1), LocalTime.MAX).withNano(0),
                        LocalDateTime.of(lastMonth.withDayOfMonth(lastMonth.lengthOfMonth()), LocalTime.MAX).withNano(0)
                );
                break;

        }
        return result;
    }

    public static String daysParseString(Integer day) {
        String result = null;
        switch (day) {
            case 1:
                result = "월요일";
                break;
            case 2:
                result = "화요일";
                break;
            case 3:
                result = "수요일";
                break;
            case 4:
                result = "목요일";
                break;
            case 5:
                result = "금요일";
                break;
            case 6:
                result = "토요일";
                break;
            case 7:
                result = "일요일";
                break;
        }
        return result;
    }
}
