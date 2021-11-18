package com.sollertia.habit.domain.notice.entiy;

import com.sollertia.habit.domain.notice.dto.NoticeVo;
import com.sollertia.habit.global.utils.TimeStamped;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Notice extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private void setTitle(String title) {
        this.title = title;
    }

    public static Notice create (NoticeVo noticeVo){
        Notice notice = new Notice();
        notice.setTitle(noticeVo.getTitle());
        return notice;
    }

}
