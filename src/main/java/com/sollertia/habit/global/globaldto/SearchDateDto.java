package com.sollertia.habit.global.globaldto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class SearchDateDto {

    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchStartDate;

    @DateTimeFormat(pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime searchEndDate;

    @QueryProjection
    public SearchDateDto(LocalDateTime searchStartDate, LocalDateTime searchEndDate) {
        this.searchStartDate = searchStartDate;
        this.searchEndDate = searchEndDate;
    }
}
