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

    private void setContents(String contents) {
        this.contents = contents;
    }

    private void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
    }

    private void setValue(String value) {
        this.value = value;
    }

    public Statistics(String contents, String value, SessionType sessionType){
        setContents(contents);
        setSessionType(sessionType);
        setValue(value);
    }

}
