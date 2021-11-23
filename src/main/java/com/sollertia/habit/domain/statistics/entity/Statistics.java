package com.sollertia.habit.domain.statistics.entity;

import com.sollertia.habit.domain.statistics.SessionType;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Statistics {
    @Id
    @GeneratedValue
    private Long id;

    private String contents;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;


}
