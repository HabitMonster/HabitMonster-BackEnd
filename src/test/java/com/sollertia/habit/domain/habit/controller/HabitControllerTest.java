package com.sollertia.habit.domain.habit.controller;

import com.sollertia.habit.domain.habit.dto.*;
import com.sollertia.habit.domain.habit.entity.Habit;
import com.sollertia.habit.domain.habit.entity.HabitWithCounter;
import com.sollertia.habit.domain.habit.service.HabitService;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.utils.DefaultResponseDto;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HabitController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(PowerMockRunner.class)
public class HabitControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private HabitServiceImpl habitService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;

    User testUser;
    UserDetailsImpl mockUserDetails;
    SecurityContext securityContext;
    Habit habit;
    List<Habit> habitList;
    LocalDate today;
    HabitTypeDto habitTypeDto;

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        mockUserDetails = new UserDetailsImpl(testUser);
        today = LocalDate.now();

        HabitDtoImpl habitDto = HabitDtoImpl.builder()
                .title("test")
                .description("testDescription")
                .durationStart("2021-11-17")
                .durationEnd("2022-11-17")
                .categoryId(2l)
                .practiceDays("1234567")
                .count(10)
                .build();

        habitTypeDto = new HabitTypeDto("counter", "specificDay");
        habit = (HabitWithCounter) Habit.createHabit(habitTypeDto.getHabitType(), habitDto, testUser);
        Whitebox.setInternalState(habit, "id", 1l);
        habitList = new ArrayList<>();
        habitList.add(habit);

    }

    private void authenticated() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUserDetails, "", mockUserDetails.getAuthorities());
        securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);
    }

    @Test
    public void HabitSummaryListResponseDtdTest() throws Throwable {
        //given
        authenticated();
        List<HabitSummaryDto> habitSummaryDtos = HabitSummaryDto.listOf(habitList);
        HabitSummaryListResponseDto returnDtos = HabitSummaryListResponseDto.builder()
                .habits(habitSummaryDtos)
                .totalHabitCount(habitSummaryDtos.size())
                .responseMessage("Habit Detail List Query Completed")
                .statusCode(200)
                .build();
        //when
        given(habitService.getHabitSummaryList(testUser, today)).willReturn(returnDtos);
        mvc.perform(get("/user/habits"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habits[0].habitId").value(habitSummaryDtos.get(0).getHabitId()))
                .andExpect(jsonPath("$.habits[0].title").value(habitSummaryDtos.get(0).getTitle()))
                .andExpect(jsonPath("$.habits[0].description").value(habitSummaryDtos.get(0).getDescription()))
                .andExpect(jsonPath("$.habits[0].durationStart").value(habitSummaryDtos.get(0).getDurationStart()))
                .andExpect(jsonPath("$.habits[0].durationEnd").value(habitSummaryDtos.get(0).getDurationEnd()))
                .andExpect(jsonPath("$.habits[0].count").value(habitSummaryDtos.get(0).getCount()))
                .andExpect(jsonPath("$.habits[0].current").value(habitSummaryDtos.get(0).getCurrent()))
                .andExpect(jsonPath("$.habits[0].practiceDays").value(habitSummaryDtos.get(0).getPracticeDays()))
                .andExpect(jsonPath("$.habits[0].isAccomplished").value(habitSummaryDtos.get(0).getIsAccomplished()))
                .andExpect(jsonPath("$.habits[0].achievePercentage").value(habitSummaryDtos.get(0).getAchievePercentage()))
                .andExpect(jsonPath("$.habits[0].category").value(habitSummaryDtos.get(0).getCategory().toString()))
                .andExpect(jsonPath("$.habits[0].achieveCount").value(habitSummaryDtos.get(0).getAchieveCount()))
                .andExpect(jsonPath("$.habits[0].totalCount").value(habitSummaryDtos.get(0).getTotalCount()))
                .andExpect(jsonPath("$.responseMessage").value(returnDtos.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(returnDtos.getStatusCode()))
                .andExpect(jsonPath("$.totalHabitCount").value(returnDtos.getTotalHabitCount()));

        verify(habitService).getHabitSummaryList(testUser, today);
    }

    @Test
    public void habitCheckTest() throws Exception {
        //given
        authenticated();
        habit.check(1l);
        HabitSummaryDto habitSummaryDto = HabitSummaryDto.of(habit);
        HabitCheckResponseDto returnDto = HabitCheckResponseDto
                .builder()
                .habit(habitSummaryDto)
                .responseMessage("Check Habit Completed")
                .statusCode(200)
                .build();
        given(habitService.checkHabit(any(), eq(1l), any())).willReturn(returnDto);

        mvc.perform(get("/habits/check/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habit.habitId").value(returnDto.getHabit().getHabitId()))
                .andExpect(jsonPath("$.habit.title").value(returnDto.getHabit().getTitle()))
                .andExpect(jsonPath("$.habit.description").value(returnDto.getHabit().getDescription()))
                .andExpect(jsonPath("$.habit.durationStart").value(returnDto.getHabit().getDurationStart()))
                .andExpect(jsonPath("$.habit.durationEnd").value(returnDto.getHabit().getDurationEnd()))
                .andExpect(jsonPath("$.habit.count").value(returnDto.getHabit().getCount()))
                .andExpect(jsonPath("$.habit.current").value(returnDto.getHabit().getCurrent()))
                .andExpect(jsonPath("$.habit.practiceDays").value(returnDto.getHabit().getPracticeDays()))
                .andExpect(jsonPath("$.habit.isAccomplished").value(returnDto.getHabit().getIsAccomplished()))
                .andExpect(jsonPath("$.habit.achievePercentage").value(returnDto.getHabit().getAchievePercentage()))
                .andExpect(jsonPath("$.habit.category").value(returnDto.getHabit().getCategory().toString()))
                .andExpect(jsonPath("$.habit.achieveCount").value(returnDto.getHabit().getAchieveCount()))
                .andExpect(jsonPath("$.habit.totalCount").value(returnDto.getHabit().getTotalCount()))
                .andExpect(jsonPath("$.responseMessage").value(returnDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(returnDto.getStatusCode()));

        verify(habitService).checkHabit(any(), eq(1l), any());

    }

    @Test
    public void habitUpdateTest() throws Exception {
        //given
        authenticated();
        HabitUpdateRequestDto updateRequestDto = new HabitUpdateRequestDto("updated title", "updated description", 2);
        habit.updateHabit(updateRequestDto);
        HabitDetail habitDetail = HabitDetail.of(habit);
        HabitDetailResponseDto returnDto = HabitDetailResponseDto.builder()
                .habit(habitDetail)
                .responseMessage("Habit updated")
                .statusCode(200)
                .build();
        given(habitService.updateHabit(any(), eq(1l), any(), any())).willReturn(returnDto);

        String json = "{\n" +
                "  \"title\": \"updated title\",\n" +
                "  \"description\": \"updated description\",\n" +
                "  \"count\": 2\n" +
                "}";
        //when
        mvc.perform(patch("/habits/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habit.habitId").value(returnDto.getHabit().getHabitId()))
                .andExpect(jsonPath("$.habit.title").value(returnDto.getHabit().getTitle()))
                .andExpect(jsonPath("$.habit.description").value(returnDto.getHabit().getDescription()))
                .andExpect(jsonPath("$.habit.durationStart").value(returnDto.getHabit().getDurationStart()))
                .andExpect(jsonPath("$.habit.durationEnd").value(returnDto.getHabit().getDurationEnd()))
                .andExpect(jsonPath("$.habit.count").value(returnDto.getHabit().getCount()))
                .andExpect(jsonPath("$.habit.practiceDays").value(returnDto.getHabit().getPracticeDays()))
                .andExpect(jsonPath("$.habit.category").value(returnDto.getHabit().getCategory().toString()))
                .andExpect(jsonPath("$.habit.achieveCount").value(returnDto.getHabit().getAchieveCount()))
                .andExpect(jsonPath("$.habit.totalCount").value(returnDto.getHabit().getTotalCount()))
                .andExpect(jsonPath("$.responseMessage").value(returnDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(returnDto.getStatusCode()));
        //then

    }

    @Test
    public void getHabitDetailTest() throws Exception {
        //given
        authenticated();
        HabitDetail habitDetail = HabitDetail.of(habit);
        HabitDetailResponseDto returnDto = HabitDetailResponseDto.builder()
                .habit(habitDetail)
                .responseMessage("Habit updated")
                .statusCode(200)
                .build();
        given(habitService.getHabitDetail(any(), eq(1l))).willReturn(returnDto);

        mvc.perform(get("/habits/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habit.habitId").value(returnDto.getHabit().getHabitId()))
                .andExpect(jsonPath("$.habit.title").value(returnDto.getHabit().getTitle()))
                .andExpect(jsonPath("$.habit.description").value(returnDto.getHabit().getDescription()))
                .andExpect(jsonPath("$.habit.durationStart").value(returnDto.getHabit().getDurationStart()))
                .andExpect(jsonPath("$.habit.durationEnd").value(returnDto.getHabit().getDurationEnd()))
                .andExpect(jsonPath("$.habit.count").value(returnDto.getHabit().getCount()))
                .andExpect(jsonPath("$.habit.practiceDays").value(returnDto.getHabit().getPracticeDays()))
                .andExpect(jsonPath("$.habit.category").value(returnDto.getHabit().getCategory().toString()))
                .andExpect(jsonPath("$.habit.achieveCount").value(returnDto.getHabit().getAchieveCount()))
                .andExpect(jsonPath("$.habit.totalCount").value(returnDto.getHabit().getTotalCount()))
                .andExpect(jsonPath("$.responseMessage").value(returnDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(returnDto.getStatusCode()));
    }

    @Test
    public void deleteHabitTest() throws Exception {
        //given
        authenticated();
        DefaultResponseDto returnDto = DefaultResponseDto.builder()
                .responseMessage("Habit Delete Completed")
                .statusCode(200)
                .build();
        given(habitService.deleteHabit(any(), eq(1l), any())).willReturn(returnDto);
        //when
        mvc.perform(delete("/habits/1"))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value(returnDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(returnDto.getStatusCode()));
    }
}
