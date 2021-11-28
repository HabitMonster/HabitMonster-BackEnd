package com.sollertia.habit.domain.notice.service;

import com.sollertia.habit.domain.notice.dto.NoticeDto;
import com.sollertia.habit.domain.notice.dto.NoticeResponseDto;
import com.sollertia.habit.domain.notice.entiy.Notice;
import com.sollertia.habit.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class NoticeServiceImplTest {

    @InjectMocks
    private NoticeServiceImpl noticeService;

    @Mock
    private NoticeRepository noticeRepository;


    @Test
    void getNotice(){
        //given
        NoticeDto noticeDto = NoticeDto.builder().id(1L).title("title").build();
        Notice notice = Notice.create(noticeDto);
        Whitebox.setInternalState(notice, "createdAt", LocalDateTime.now());
        List<Notice> notices = new ArrayList<>();
        notices.add(notice);
        given(noticeRepository.findAll()).willReturn(notices);

        //when
        NoticeResponseDto noticeResponseDto = noticeService.getNoticeList();

        //then
        assertThat(noticeResponseDto.getNotices().get(0).getTitle()).isEqualTo(notices.get(0).getTitle());
        assertThat(noticeResponseDto.getStatusCode()).isEqualTo(200);
        assertThat(noticeResponseDto.getResponseMessage()).isEqualTo("Notice Query Completed");

    }


}