package com.sollertia.habit.domain.monster.service;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.service.UserService;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MonsterServiceTest {

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Mock
    private UserService userService;
    @Mock
    private MonsterCollectionService monsterCollectionService;

    User testUser;
    User updatedTestUser;
    Monster monster1;
    Monster monster2;
    List<MonsterDatabase> mockMonsterDatabaseList = new ArrayList<>();

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);
        updatedTestUser = User.create(oauth2UserInfo);

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(EvolutionGrade.EV1, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(EvolutionGrade.EV1, "dog.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);

        mockMonsterDatabaseList.add(monsterDatabase1);
        mockMonsterDatabaseList.add(monsterDatabase2);
    }

    @Test
    void getAllMonsters() {
        //given
        given(monsterDatabaseRepository.findAllByGrade(EvolutionGrade.EV1))
                .willReturn(mockMonsterDatabaseList);

        //when
        MonsterListResponseDto responseDto = monsterService.getAllMonsters(testUser);

        //then
        assertThat(responseDto.getMonsters().get(0).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(0).getImageUrl());
        assertThat(responseDto.getMonsters().get(1).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(1).getImageUrl());
        assertThat(responseDto.getMonsters().get(2).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(2).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("LV1 몬스터를 불러오는데 성공했습니다.");

        verify(monsterDatabaseRepository).findAllByGrade(EvolutionGrade.EV1);
    }

    @Test
    void updateMonster() {
        //given
        updatedTestUser.updateMonster(monster1);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterDatabaseList.get(0)));
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto(1L, monster1.getName());
        given(userService.updateMonster(eq(testUser), any(Monster.class)))
                .willReturn(updatedTestUser);

        //when
        MonsterResponseDto responseDto = monsterService.updateMonster(testUser, mockRequestDto);

        //then
        assertThat(responseDto.getMonster().getMonsterImage())
                .isEqualTo(monster1.getMonsterDatabase().getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(mockRequestDto.getMonsterName());
        assertThat(responseDto.getMonster().getMonsterLevel()).isEqualTo(1);
        assertThat(responseDto.getMonster().getMonsterExpPoint()).isEqualTo(0L);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터가 선택되었습니다.");

        verify(monsterDatabaseRepository).findById(1L);
        verify(userService).updateMonster(eq(testUser), any(Monster.class));
    }

    @Test
    void updateMonsterIfUserHasMonster() {
        //given
        testUser.updateMonster(monster1);
        testUser.getMonster().levelUp();
        testUser.getMonster().levelUp();
        testUser.getMonster().levelUp();
        testUser.getMonster().levelUp();
        updatedTestUser.updateMonster(monster2);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterDatabaseList.get(1)));
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(monster1));
        given(userService.updateMonster(eq(testUser), any(Monster.class)))
                .willReturn(updatedTestUser);
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto(1L, monster2.getName());

        //when
        MonsterResponseDto responseDto = monsterService.updateMonster(testUser, mockRequestDto);

        //then
        assertThat(responseDto.getMonster().getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(1).getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(mockRequestDto.getMonsterName());
        assertThat(responseDto.getMonster().getMonsterName()).isEqualTo(monster2.getName());
        assertThat(responseDto.getMonster().getMonsterLevel()).isEqualTo(1);
        assertThat(responseDto.getMonster().getMonsterExpPoint()).isEqualTo(0L);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터가 선택되었습니다.");

        verify(monsterDatabaseRepository).findById(1L);
        verify(monsterRepository).findById(any());
        verify(userService).updateMonster(eq(testUser), any(Monster.class));
    }

    @Test
    void updateMonsterNotInDatabase() {
        //given
        updatedTestUser.updateMonster(monster1);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.empty());
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto(1L, monster1.getName());

        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.updateMonster(testUser, mockRequestDto));

        verify(monsterDatabaseRepository).findById(1L);
        verify(userService, never()).updateMonster(any(), any());
    }

    @Test
    void getMonsterVo() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(monster1));

        //when
        MonsterVo monsterVo = monsterService.getMonsterVo(testUser);

        //then
        assertThat(monsterVo.getMonsterName()).isEqualTo(testUser.getMonster().getName());
        assertThat(monsterVo.getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(0).getImageUrl());

        verify(monsterRepository).findById(any());
    }

    @Test
    void plusExpPoint() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(monster1));

        //when
        monsterService.plusExpPoint(testUser);

        //then
        assertThat(monster1.getExpPoint()).isEqualTo(monster1.getLevel().getPlusPoint().intValue());
        verify(monsterRepository).findById(any());
    }

    @Test
    void getMonsterResponseDtoFromUser() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(monster1));

        //when
        MonsterResponseDto responseDto =  monsterService.getMonsterResponseDtoFromUser(testUser);

        //then
        assertThat(responseDto.getMonster().getMonsterImage()).isEqualTo("cat.img");
        assertThat(responseDto.getMonster().getMonsterName()).isEqualTo("고양이");
        assertThat(responseDto.getMonster().getMonsterLevel()).isEqualTo(1);
        assertThat(responseDto.getMonster().getMonsterExpPoint()).isEqualTo(0L);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("사용자 몬스터 조회 성공");
        verify(monsterRepository).findById(any());
    }

    @Test
    void getMonsterResponseDtoFromUserHasNotMonster() {
        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.getMonsterResponseDtoFromUser(testUser));
        verify(monsterRepository, never()).findById(any());
    }

    @Test
    void getMonsterResponseDtoFromUserHasNotMonsterInDatabase() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findById(any()))
                .willReturn(Optional.empty());

        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.getMonsterResponseDtoFromUser(testUser));
        verify(monsterRepository).findById(any());
    }

}