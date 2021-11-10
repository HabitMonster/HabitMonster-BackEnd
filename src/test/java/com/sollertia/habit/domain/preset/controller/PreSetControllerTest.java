package com.sollertia.habit.domain.preset.controller;

import com.sollertia.habit.domain.category.enums.Category;
import com.sollertia.habit.domain.habit.dto.HabitDtoImpl;
import com.sollertia.habit.domain.habit.service.HabitServiceImpl;
import com.sollertia.habit.domain.preset.dto.PreSetResponseDto;
import com.sollertia.habit.domain.preset.dto.PreSetVo;
import com.sollertia.habit.domain.preset.presetservice.PreSetServiceImpl;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @DisplayName("category별 PreSet 조회")
    @Test
    void categoryPreSet() throws Exception {
        //given
        authenticated();
        List<PreSetVo> list = new ArrayList<>();
        list.add(PreSetVo.builder().presetId(1L).categoryId(1L).title("title").description("description").
                period(30).count(3).category(Category.Health).practiceDays("12345").build());

        given(preSetService.categoryPreSetList(anyLong())).willReturn(list);
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
                .andExpect(jsonPath("$.responseMessage").value("PreSets Query completed"))
                .andExpect(jsonPath("$.statusCode").value(200));

        verify(preSetService).categoryPreSetList(anyLong());
    }

}
