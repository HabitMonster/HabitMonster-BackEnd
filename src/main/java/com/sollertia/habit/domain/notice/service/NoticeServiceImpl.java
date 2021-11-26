package com.sollertia.habit.domain.notice.service;

import com.sollertia.habit.domain.notice.dto.NoticeResponseDto;
import com.sollertia.habit.domain.notice.dto.NoticeDto;
import com.sollertia.habit.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeResponseDto getNoticeList() {
        List<NoticeDto> noticeDtoList = noticeRepository.findAll().stream().map(NoticeDto::of)
                .collect(Collectors.toCollection(ArrayList::new));

        return NoticeResponseDto.builder()
                .notices(noticeDtoList)
                .responseMessage("Notice Query Completed")
                .statusCode(200)
                .build();
    }
}