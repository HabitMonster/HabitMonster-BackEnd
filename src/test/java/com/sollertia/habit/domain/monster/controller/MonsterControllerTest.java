package com.sollertia.habit.domain.monster.controller;

import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.monster.service.MonsterCollectionService;
import com.sollertia.habit.domain.monster.service.MonsterService;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.security.jwt.filter.JwtTokenProvider;
import com.sollertia.habit.domain.user.security.userdetail.UserDetailsImpl;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import com.sollertia.habit.global.utils.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MonsterController.class)
@AutoConfigureMockMvc(addFilters = false)
@RunWith(PowerMockRunner.class)
class MonsterControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MonsterService monsterService;
    @MockBean
    private JwtTokenProvider jwtTokenProvider;
    @MockBean
    private RedisUtil redisUtil;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private MonsterCollectionService monsterCollectionService;

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
    void getAllMonsters() throws Exception {
        //given
        authenticated();
        List<MonsterSummaryVo> summaryVoList = new ArrayList<>();
        summaryVoList.add(MonsterSummaryVo.builder().monsterId(1L).monsterImage("monster.img").build());
        MonsterListResponseDto responseDto = MonsterListResponseDto.builder().monsters(summaryVoList).responseMessage("LV1 Monster Query Completed").statusCode(200).build();

        given(monsterService.getAllMonsters(testUser))
                .willReturn(responseDto);
        //when
        mvc.perform(get("/monsters"))
                .andDo(print())
        //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monsters[0].monsterId").value("1"))
                .andExpect(jsonPath("$.monsters[0].monsterImage").value("monster.img"))
                .andExpect(jsonPath("$.responseMessage").value("LV1 Monster Query Completed"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(monsterService).getAllMonsters(testUser);
    }

    @Test
    void updateMonster() throws Exception {
        //given
        authenticated();
        MonsterResponseDto responseDto = MonsterResponseDto.builder()
                .monster(MonsterVo.builder().monsterImage("monster.img").monsterName("testmonster").build())
                .responseMessage("Selected Monster")
                .statusCode(200).build();

        given(monsterService.updateMonster(eq(testUser), any(MonsterSelectRequestDto.class)))
                .willReturn(responseDto);

        String json = "{\n" +
                "  \"monsterId\": 1,\n" +
                "  \"monsterName\": \"mycat\"\n" +
                "}";

        //when
        mvc.perform(patch("/user/monster")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monster.monsterImage").value("monster.img"))
                .andExpect(jsonPath("$.monster.monsterName").value("testmonster"))
                .andExpect(jsonPath("$.responseMessage").value("Selected Monster"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(monsterService).updateMonster(eq(testUser), any(MonsterSelectRequestDto.class));
    }

    @Test
    void updateMonsterName() throws Exception {
        //given
        authenticated();
        MonsterResponseDto responseDto = MonsterResponseDto.builder()
                .monster(MonsterVo.builder().monsterImage("monster.img").monsterName("testmonster").build())
                .responseMessage("Change Monster Name")
                .statusCode(200).build();

        given(monsterService.updateMonsterName(eq(testUser), any(MonsterSelectRequestDto.class)))
                .willReturn(responseDto);

        String json = "{\n" +
                "  \"monsterId\": 1,\n" +
                "  \"monsterName\": \"mycat\"\n" +
                "}";

        //when
        mvc.perform(patch("/monster/nameChange")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monster.monsterImage").value("monster.img"))
                .andExpect(jsonPath("$.monster.monsterName").value("testmonster"))
                .andExpect(jsonPath("$.responseMessage").value("Change Monster Name"))
                .andExpect(jsonPath("$.statusCode").value("200"));

        verify(monsterService).updateMonsterName(eq(testUser), any(MonsterSelectRequestDto.class));
    }

    @Test
    void getMonsterCollection() throws Exception {
        //given
        authenticated();
        List<MonsterCollectionVo> monsterCollectionVoList = new ArrayList<>();
        List<MonsterDatabaseVo> monsterDatabases = new ArrayList<>();
        Monster mockMonster = mock(Monster.class);
        given(mockMonster.getCreatedAt()).willReturn(LocalDateTime.now());
        given(mockMonster.getLevel()).willReturn(Level.LV1);
        MonsterDatabase mockMonsterDatabase = mock(MonsterDatabase.class);
        given(mockMonsterDatabase.getMonsterType()).willReturn(MonsterType.BLUE);
        given(mockMonster.getMonsterDatabase()).willReturn(mockMonsterDatabase);
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(mockMonster);
        monsterCollectionVoList.add(MonsterCollectionVo.of(monsterCollection, monsterDatabases));
        MonsterCollectionResponseDto responseDto = MonsterCollectionResponseDto.builder()
                .monsters(monsterCollectionVoList)
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200).build();

        given(monsterCollectionService.getMonsterCollection(testUser))
                .willReturn(responseDto);
        //when
        mvc.perform(get("/user/monsters"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseMessage").value(responseDto.getResponseMessage()))
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()));

        verify(monsterCollectionService).getMonsterCollection(testUser);
    }

    @Test
    void getMonsterResponseDtoFromUser() throws Exception {
        //given
        authenticated();
        MonsterResponseDto responseDto = MonsterResponseDto.builder()
                .monster(MonsterVo.builder().monsterImage("monster.img").monsterName("testmonster").build())
                .responseMessage("Selected Monster")
                .statusCode(200).build();

        given(monsterService.getMonsterResponseDtoFromUser(testUser))
                .willReturn(responseDto);

        //when
        mvc.perform(get("/user/monster"))
                .andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monster.monsterImage").value("monster.img"))
                .andExpect(jsonPath("$.monster.monsterName").value("testmonster"))
                .andExpect(jsonPath("$.responseMessage").value("Selected Monster"))
                .andExpect(jsonPath("$.statusCode").value("200"));
    }

    @Test
    void getMonsterResponseDtoFromUseHasNotMonster() throws Exception {
        //given
        authenticated();
        String errorMessage = "NotFound User of Selected Monster";
        willThrow(new MonsterNotFoundException(errorMessage)).given(monsterService)
                        .getMonsterResponseDtoFromUser(testUser);

        //when
        mvc.perform(get("/user/monster"))
                .andDo(print())
                //then
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseMessage").value(errorMessage))
                .andExpect(jsonPath("$.statusCode").value("404"));
    }
}