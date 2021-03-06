package com.sollertia.habit.domain.notice.controller;

import com.sollertia.habit.domain.notice.dto.NoticeResponseDto;
import com.sollertia.habit.domain.notice.service.NoticeServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class NoticeController {

    private final NoticeServiceImpl noticeService;

    @ApiOperation(value = "공지사항", notes = "공지사항 리스트")
    @GetMapping("/notice")
    public NoticeResponseDto getNoticeList(){
        return noticeService.getNoticeList();
    }
}
