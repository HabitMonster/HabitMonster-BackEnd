package com.sollertia.habit.domain.monster.service;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.repository.UserRepository;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import com.sollertia.habit.global.exception.monster.NotReachedMaximumLevelException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class MonsterServiceTest {

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterDatabaseRepository monsterDatabaseRepository;
    @Mock
    private MonsterCollectionService monsterCollectionService;
    @Mock
    private UserRepository userRepository;

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

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1, MonsterType.RED, "dog.img");
        monster1 = Monster.createNewMonster("?????????", monsterDatabase1);
        monster2 = Monster.createNewMonster("?????????", monsterDatabase2);

        Whitebox.setInternalState(monster1, "createdAt", LocalDateTime.now());
        Whitebox.setInternalState(monster2, "createdAt", LocalDateTime.now());

        mockMonsterDatabaseList.add(monsterDatabase1);
        mockMonsterDatabaseList.add(monsterDatabase2);
    }

    @Test
    void getAllMonsters() {
        //given
        given(monsterDatabaseRepository.findAllByLevel(Level.LV1))
                .willReturn(mockMonsterDatabaseList);

        //when
        MonsterListResponseDto responseDto = monsterService.getAllMonsters(testUser);

        //then
        assertThat(responseDto.getMonsters().get(0).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(0).getImageUrl());
        assertThat(responseDto.getMonsters().get(1).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(1).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("LV1 Monster Query Completed");

        verify(monsterDatabaseRepository).findAllByLevel(Level.LV1);
    }

    @Test
    void updateMonsterName() {
        //given
        updatedTestUser.updateMonster(monster1);
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto();
        Whitebox.setInternalState(mockRequestDto, "monsterId", 1L);
        Whitebox.setInternalState(mockRequestDto, "monsterName", monster1.getName());

        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.ofNullable(monster1));

        //when
        MonsterResponseDto responseDto = monsterService.updateMonsterName(testUser, mockRequestDto);

        //then
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(mockRequestDto.getMonsterName());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Change Monster Name");

        verify(monsterRepository).findByUserId(any());
    }

    @Test
    void updateMonster() {
        //given
        updatedTestUser.updateMonster(monster1);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterDatabaseList.get(0)));
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto();
        Whitebox.setInternalState(mockRequestDto, "monsterId", 1L);
        Whitebox.setInternalState(mockRequestDto, "monsterName", monster1.getName());
        given(monsterRepository.save(any(Monster.class)))
                .willReturn(monster1);

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
        assertThat(responseDto.getResponseMessage()).isEqualTo("Selected Monster");

        verify(monsterDatabaseRepository).findById(1L);
        verify(monsterRepository).save(any(Monster.class));
    }

    @Test
    void updateMonsterIfUserHasMonster() {
        //given
        Whitebox.setInternalState(monster1, "level", Level.LV5);
        Whitebox.setInternalState(monster1, "expPoint", Level.MAX_EXP);
        testUser.updateMonster(monster1);
        updatedTestUser.updateMonster(monster2);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterDatabaseList.get(1)));
        given(monsterRepository.save(any(Monster.class)))
                .willReturn(monster2);
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto();
        Whitebox.setInternalState(mockRequestDto, "monsterId", 1L);
        Whitebox.setInternalState(mockRequestDto, "monsterName", monster2.getName());

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
        assertThat(responseDto.getResponseMessage()).isEqualTo("Selected Monster");

        verify(monsterDatabaseRepository).findById(1L);
        verify(monsterCollectionService).addMonsterCollection(any(Monster.class));
        verify(monsterRepository).save(any(Monster.class));
    }

    @Test
    void updateMonsterIfUserHasMonsterNotReachedMaximumLevel() {
        //given
        Whitebox.setInternalState(monster1, "level", Level.LV3);
        Whitebox.setInternalState(monster1, "expPoint", Level.MAX_EXP);
        testUser.updateMonster(monster1);

        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto();
        Whitebox.setInternalState(mockRequestDto, "monsterId", 1L);
        Whitebox.setInternalState(mockRequestDto, "monsterName", monster2.getName());

        //when, then
        assertThrows(NotReachedMaximumLevelException.class,
                () -> monsterService.updateMonster(testUser, mockRequestDto));
        verify(monsterDatabaseRepository, never()).findById(1L);
        verify(monsterCollectionService, never()).addMonsterCollection(any(Monster.class));
        verify(monsterRepository, never()).save(any(Monster.class));
    }

    @Test
    void updateMonsterNotInDatabase() {
        //given
        updatedTestUser.updateMonster(monster1);
        given(monsterDatabaseRepository.findById(1L))
                .willReturn(Optional.empty());
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto();
        Whitebox.setInternalState(mockRequestDto, "monsterId", 1L);
        Whitebox.setInternalState(mockRequestDto, "monsterName", monster1.getName());

        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.updateMonster(testUser, mockRequestDto));

        verify(monsterDatabaseRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void plusExpPoint() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));

        //when
        monsterService.plusExpPoint(testUser);

        //then
        assertThat(monster1.getExpPoint()).isEqualTo(monster1.getLevel().getPlusPoint().intValue());
        verify(monsterRepository).findByUserId(any());
    }

    @Test
    void plusExpPointLevelUp() {
        //given
        Level level = monster1.getLevel();
        testUser.updateMonster(monster1);
        Whitebox.setInternalState(monster1, "expPoint", 99L);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));
        given(monsterDatabaseRepository
                .findByMonsterTypeAndLevel(monster1.getMonsterDatabase().getMonsterType(), Level.nextOf(level)))
                .willReturn(Optional.of(mockMonsterDatabaseList.get(1)));
        //when
        monsterService.plusExpPoint(testUser);

        //then
        assertThat(monster1.getLevel()).isEqualTo(Level.nextOf(level));
        assertThat(monster1.getMonsterDatabase()).isEqualTo(mockMonsterDatabaseList.get(1));
        assertThat(monster1.getExpPoint()).isEqualTo(level.getPlusPoint()-1);

        verify(monsterRepository).findByUserId(any());
        verify(monsterDatabaseRepository).
                findByMonsterTypeAndLevel(mockMonsterDatabaseList.get(0).getMonsterType(), monster1.getLevel());

    }

    @Test
    void plusExpPointLevelUpMonsterDBNotFound() {
        //given
        Level level = monster1.getLevel();
        testUser.updateMonster(monster1);
        Whitebox.setInternalState(monster1, "expPoint", 99L);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));
        given(monsterDatabaseRepository
                .findByMonsterTypeAndLevel(monster1.getMonsterDatabase().getMonsterType(), Level.nextOf(level)))
                .willReturn(Optional.empty());

        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.plusExpPoint(testUser));
        verify(monsterRepository).findByUserId(any());
        verify(monsterDatabaseRepository).
                findByMonsterTypeAndLevel(monster1.getMonsterDatabase().getMonsterType(), monster1.getLevel());

    }

    @Test
    void minusExpWithCount() {
        //given
        testUser.updateMonster(monster1);
        Whitebox.setInternalState(monster1, "expPoint", 90L);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));

        //when
        monsterService.minusExpWithCount(testUser, 3L);

        //then
        assertThat(monster1.getExpPoint()).isEqualTo(90L - 3L * monster1.getLevel().getMinusPoint());
        verify(monsterRepository).findByUserId(any());
    }

    @Test
    void minusExpWithCountZero() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));

        //when
        monsterService.minusExpWithCount(testUser, 3L);

        //then
        assertThat(monster1.getExpPoint()).isEqualTo(monster1.getExpPoint());
        verify(monsterRepository).findByUserId(any());
    }

    @Test
    void getMonsterResponseDtoFromUser() {
        //given
        testUser.updateMonster(monster1);
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.of(monster1));

        //when
        MonsterResponseDto responseDto =  monsterService.getMonsterResponseDtoFromUser(testUser);

        //then
        assertThat(responseDto.getMonster().getMonsterImage()).isEqualTo("cat.img");
        assertThat(responseDto.getMonster().getMonsterName()).isEqualTo("?????????");
        assertThat(responseDto.getMonster().getMonsterLevel()).isEqualTo(1);
        assertThat(responseDto.getMonster().getMonsterExpPoint()).isEqualTo(0L);
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("User Monster Query Completed");
        verify(monsterRepository).findByUserId(any());
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
        given(monsterRepository.findByUserId(any()))
                .willReturn(Optional.empty());

        //when, then
        assertThrows(MonsterNotFoundException.class,
                () -> monsterService.getMonsterResponseDtoFromUser(testUser));
        verify(monsterRepository).findByUserId(any());
    }

}