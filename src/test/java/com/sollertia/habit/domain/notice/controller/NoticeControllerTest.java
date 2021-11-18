package com.sollertia.habit.domain.notice.controller;

import com.sollertia.habit.domain.notice.dto.NoticeResponseDto;
import com.sollertia.habit.domain.notice.dto.NoticeVo;
import com.sollertia.habit.domain.notice.service.NoticeServiceImpl;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    User testUser;
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        mockUserDetails = new UserDetailsImpl(testUser);
    }

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void noticeTest() throws Exception {

        //given
        authenticated();
        NoticeVo noticeVo = NoticeVo.builder().title("title").build();
        List<NoticeVo> noticeVoList = new ArrayList<>();
        noticeVoList.add(noticeVo);
        NoticeResponseDto noticeResponseDto = NoticeResponseDto.builder().notices(noticeVoList).statusCode(200).responseMessage("Notice Query Completed").build();
        given(noticeService.getNoticeList()).willReturn(noticeResponseDto);

        //when
        mvc.perform(get("/notice"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notices[0].title").value("title"))
                .andExpect(jsonPath("$.responseMessage").value("Notice Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(noticeService).getNoticeList();

    }

}