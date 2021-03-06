package com.sollertia.habit.domain.preset.controller;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDetail;
import com.sollertia.habit.domain.habit.dto.HabitDetailResponseDto;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.preset.dto.PreSetDto;
import com.sollertia.habit.domain.preset.service.PreSetServiceImpl;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.exception.preset.PreSetNotFoundException;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PreSetController.class)
@AutoConfigureMockMvc(addFilters = false)
class PreSetControllerTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private PreSetServiceImpl preSetService;
    @MockBean
    private HabitServiceImpl habitService;

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

    @DisplayName("category??? PreSet ??????")
    @Test
    void categoryPreSet() throws Exception {
        //given
        authenticated();
        List<PreSetDto> list = new ArrayList<>();
        list.add(PreSetDto.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build());

        given(preSetService.categoryPreSetList(1L)).willReturn(list);
        //when
        mvc.perform(get("/categories/1/presets"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.preSets[0].presetId").value(1L))
                .andExpect(jsonPath("$.preSets[0].categoryId").value(1L))
                .andExpect(jsonPath("$.preSets[0].title").value("title"))
                .andExpect(jsonPath("$.preSets[0].description").value("description"))
                .andExpect(jsonPath("$.preSets[0].period").value(30))
                .andExpect(jsonPath("$.preSets[0].count").value(3))
                .andExpect(jsonPath("$.preSets[0].category").value("Health"))
                .andExpect(jsonPath("$.preSets[0].practiceDays").value("12345"))
                .andExpect(jsonPath("$.responseMessage").value("PreSets Query Completed"))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(preSetService).categoryPreSetList(1L);
    }

    @DisplayName("PreSet ??????")
    @Test
    void selectPreSet() throws Exception {
        //given
        authenticated();
        PreSetDto preSetDto = PreSetDto.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build();
        HabitDetail habitDetail = HabitDetail.builder()
                .habitId(1L)
                .title(preSetDto.getTitle())
                .description(preSetDto.getDescription())
                .durationStart("2021-11-01")
                .durationEnd("2021-11-30")
                .count(preSetDto.getCount())
                .category(preSetDto.getCategory())
                .practiceDays(preSetDto.getPracticeDays())
                .current(0)
                .achievePercentage(0L)
                .build();
        HabitDetailResponseDto responseDto = HabitDetailResponseDto.builder()
                .habit(habitDetail).statusCode(200).responseMessage("Habit registered Completed").build();
        given(preSetService.getPreSet(1L)).willReturn(preSetDto);
        given(habitService.createHabit(any(), any(), eq(testUser))).willReturn(responseDto);

        //when
        mvc.perform(post("/presets/1"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.habit.category").value(preSetDto.getCategory().toString()))
                .andExpect(jsonPath("$.habit.title").value(preSetDto.getTitle()))
                .andExpect(jsonPath("$.habit.description").value(preSetDto.getDescription()))
                .andExpect(jsonPath("$.habit.count").value(preSetDto.getCount()))
                .andExpect(jsonPath("$.habit.practiceDays").value(preSetDto.getPracticeDays()))
                .andExpect(jsonPath("$.responseMessage").value("Habit registered Completed"))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(preSetService).getPreSet(1L);
    }

    @DisplayName("PreSet Not Found")
    @Test
    void selectPreSetNotFound() throws Exception {
        //given
        authenticated();
        given(preSetService.getPreSet(1L))
                .willThrow(new PreSetNotFoundException("Not Found PreSet"));

        //when
        mvc.perform(post("/presets/1"))
                .andDo(print())
                //then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.responseMessage").value("Not Found PreSet"))
                .andExpect(jsonPath("$.statusCode").value(400));

        verify(preSetService).getPreSet(1L);
        verify(habitService, never()).createHabit(any(), any(), eq(testUser));
    }

}
