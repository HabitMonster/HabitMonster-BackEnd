package com.sollertia.habit.domain.completedhabbit.statics.controller;

import com.sollertia.habit.domain.completedhabbit.dto.SimpleHabitVo;
import com.sollertia.habit.domain.completedhabbit.entity.CompletedHabit;
import com.sollertia.habit.domain.completedhabbit.statics.dto.StatisticsResponseDto;
import com.sollertia.habit.domain.completedhabbit.statics.service.StatisticsServiceImpl;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.dto.HabitTypeDto;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StatisticsContoller.class)
@AutoConfigureMockMvc(addFilters = false)
class StatisticsContollerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StatisticsServiceImpl statisticsService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    User testUser;
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;
    HabitDtoImpl habitDto;
    CompletedHabit completedHabit;
    List<SimpleHabitVo> simpleHabitVoList;

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        mockUserDetails = new UserDetailsImpl(testUser);

        Calendar startDate = Calendar.getInstance();
        DateFormat form = new SimpleDateFormat("yyyy-MM-dd");

        habitDto = HabitDtoImpl.builder()
                .durationStart(form.format(startDate.getTime())).durationEnd(form.format(startDate.getTime()))
                .count(3).title("title").description("description").practiceDays("1234567").categoryId(1L).build();
        HabitTypeDto habitTypeDto = new HabitTypeDto("counter", "specificDay");
        Habit habit = Habit.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
        completedHabit = CompletedHabit.of(habit);

        simpleHabitVoList = new ArrayList<>();
        simpleHabitVoList.add(new SimpleHabitVo(completedHabit));

    }

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    void getStatistics() throws Exception {
        //given
        authenticated();
        String date = "2021-11";
        StatisticsResponseDto responseDto = StatisticsResponseDto.builder()
                .succeededCount(10)
                .failedCount(10)
                .totalCount(20)
                .habitList(simpleHabitVoList)
                .responseMessage("Statistics Query Completed").statusCode(200).build();

        given(statisticsService.getStatistics(testUser, date))
                .willReturn(responseDto);
        //when
        mvc.perform(get("/statistics")
                        .param("date", date))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount").value(responseDto.getTotalCount()))
                .andExpect(jsonPath("$.succeededCount").value(responseDto.getSucceededCount()))
                .andExpect(jsonPath("$.failedCount").value(responseDto.getFailedCount()))
                .andExpect(jsonPath("$.habitList[0].title").value(completedHabit.getTitle()))
                .andExpect(jsonPath("$.habitList[0].achievement").value(completedHabit.getAchievementPercentage()))
                .andExpect(jsonPath("$.habitList[0].accomplishCount").value(completedHabit.getAccomplishCounter()))
                .andExpect(jsonPath("$.habitList[0].goalCount").value(completedHabit.getGoalCount()))
                .andExpect(jsonPath("$.habitList[0].success").value(completedHabit.getIsSuccess()))
                .andExpect(jsonPath("$.responseMessage").value(responseDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()));

        verify(statisticsService).getStatistics(testUser, "2021-11");
    }

}