package com.sollertia.habit.domain.monster.service;

import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.entity.*;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RunWith(PowerMockRunner.class)
class MonsterCollectionServiceTest {

    @InjectMocks
    private MonsterCollectionService monsterCollectionService;

    @Mock
    private MonsterCollectionRepository monsterCollectionRepository;
    @Mock
    private MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;

    User testUser;
    Monster monster1;
    Monster monster2;
    List<MonsterDatabase> mockMonsterDatabaseList = new ArrayList<>();
    List<MonsterCollection> mockMonsterCollectionList = new ArrayList<>();

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(Level.LV1, MonsterType.BLUE, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(Level.LV1, MonsterType.RED, "dog.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);

        Whitebox.setInternalState(monster1, "createdAt", LocalDateTime.now());
        Whitebox.setInternalState(monster2, "createdAt", LocalDateTime.now());

        mockMonsterDatabaseList.add(monsterDatabase1);
        mockMonsterDatabaseList.add(monsterDatabase2);

        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(monster1));
        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(monster2));
    }

    @Test
    void addMonsterCollection() {
        //given
        given(monsterCollectionRepository.save(any(MonsterCollection.class)))
                .willReturn(mockMonsterCollectionList.get(0));
        given(monsterCollectionDatabaseRepository.save(any(MonsterCollectionDatabase.class)))
                .willReturn(MonsterCollectionDatabase.from(mockMonsterDatabaseList.get(0),mockMonsterCollectionList.get(0)));

        //when
        MonsterCollection monsterCollection = monsterCollectionService.addMonsterCollection(monster1);

        //then
        assertThat(monsterCollection.getMonsterName()).isEqualTo(monster1.getName());

        verify(monsterCollectionRepository).save(any(MonsterCollection.class));
        verify(monsterCollectionDatabaseRepository).save(any(MonsterCollectionDatabase.class));
    }

    @Test
    void getMonsterCollection() {
        //given
        given(monsterCollectionRepository.searchByUser(testUser))
                .willReturn(mockMonsterCollectionList);

        //when
        MonsterCollectionResponseDto responseDto = monsterCollectionService.getMonsterCollectionResponseDto(testUser);

        //then
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Monster Collection Query Completed");

        verify(monsterCollectionRepository).searchByUser(testUser);
    }
}