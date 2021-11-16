package com.sollertia.habit.domain.notice.controller;

import com.sollertia.habit.domain.notice.dto.NoticeResponseDto;
import com.sollertia.habit.domain.notice.dto.NoticeVo;
import com.sollertia.habit.domain.notice.service.NoticeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NoticeController.class)
class NoticeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private NoticeServiceImpl noticeService;



    @Test
    void noticeTest() throws Exception {

        //given
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder().build();
        NoticeVo noticeVo = NoticeVo.builder().
        given(noticeService.getNoticeList())
                .willReturn(noticeResponseDto);

        //when
        mvc.perform(get("/notice"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monsters[0].monsterId").value("1"))
                .andExpect(jsonPath("$.monsters[0].monsterImage").value("monster.img"))
                .andExpect(jsonPath("$.responseMessage").value("LV1 Monster Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(noticeService).getNoticeList();

    }

}