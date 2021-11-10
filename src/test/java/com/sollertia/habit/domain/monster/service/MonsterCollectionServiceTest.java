package com.sollertia.habit.domain.monster.service;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.EvolutionGrade;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.user.oauth2.userinfo.Oauth2UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MonsterCollectionServiceTest {

    @InjectMocks
    private MonsterCollectionService monsterCollectionService;

    @Mock
    private MonsterCollectionRepository monsterCollectionRepository;

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

        MonsterDatabase monsterDatabase1 = new MonsterDatabase(EvolutionGrade.EV1, "cat.img");
        MonsterDatabase monsterDatabase2 = new MonsterDatabase(EvolutionGrade.EV1, "dog.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);

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

        //when
        MonsterCollection monsterCollection = monsterCollectionService.addMonsterCollection(monster1);

        //then
        assertThat(monsterCollection.getMonsterName()).isEqualTo(monster1.getName());
        assertThat(monsterCollection.getMonsterDatabase()).isEqualTo(mockMonsterDatabaseList.get(0));
    }

    @Test
    void getMonsterCollection() {
        //given
        given(monsterCollectionRepository.findAllByUser(testUser))
                .willReturn(mockMonsterCollectionList);

        //when
        MonsterListResponseDto responseDto = monsterCollectionService.getMonsterCollection(testUser);

        //then
        assertThat(responseDto.getMonsters().get(0).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(0).getImageUrl());
        assertThat(responseDto.getMonsters().get(1).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(1).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("Monster Collection Query Completed");

        verify(monsterCollectionRepository).findAllByUser(testUser);
    }
}