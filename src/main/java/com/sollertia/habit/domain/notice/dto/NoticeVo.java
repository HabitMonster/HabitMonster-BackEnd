package com.sollertia.habit.domain.notice.dto;

import com.sollertia.habit.domain.notice.entiy.Notice;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NoticeVo {
    private Long id;
    private String title;
    private String content;

    public static NoticeVo of(Notice notice){
        return NoticeVo.builder().id(notice.getId()).title(notice.getTitle())
                        .content(notice.getContent()).build();
    }
}
