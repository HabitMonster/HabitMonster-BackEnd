package com.sollertia.habit.domain.statistics.entity;

import com.sollertia.habit.domain.statistics.enums.SessionType;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue
    private Long id;

    private String contents;

    private String value;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;
}
