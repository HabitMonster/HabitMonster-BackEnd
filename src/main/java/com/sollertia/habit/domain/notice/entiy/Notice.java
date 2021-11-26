package com.sollertia.habit.domain.notice.entiy;

import com.sollertia.habit.domain.notice.dto.NoticeDto;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private void setTitle(String title) {
        this.title = title;
    }

    public static Notice create (NoticeDto noticeDto){
        Notice notice = new Notice();
        notice.setTitle(noticeDto.getTitle());
        return notice;
    }

}
