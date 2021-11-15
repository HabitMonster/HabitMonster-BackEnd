package com.sollertia.habit.domain.notice.entiy;

import com.sollertia.habit.domain.notice.dto.NoticeVo;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Notice(NoticeVo noticeVo){
        setTitle(noticeVo.getTitle());
        setContent(noticeVo.getContent());
    }

}
