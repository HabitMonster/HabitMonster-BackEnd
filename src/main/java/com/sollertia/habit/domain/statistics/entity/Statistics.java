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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @Enumerated(EnumType.STRING)
    private SessionType sessionType;

    public void setContents(String contents) {
        this.contents = contents;
    }

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    public Statistics(String contents, SessionType sessionType){
        setContents(contents);
        setSessionType(sessionType);
    }

}
