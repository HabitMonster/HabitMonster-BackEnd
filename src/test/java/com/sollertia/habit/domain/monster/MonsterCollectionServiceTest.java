package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
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
import static org.junit.jupiter.api.Assertions.*;
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
        MonsterDatabase monsterDatabase3 = new MonsterDatabase(EvolutionGrade.EV1, "dug.img");
        monster1 = Monster.createNewMonster("고양이", monsterDatabase1);
        monster2 = Monster.createNewMonster("강아지", monsterDatabase2);
        Monster monster3 = Monster.createNewMonster("오리", monsterDatabase3);

        mockMonsterDatabaseList.add(monsterDatabase1);
        mockMonsterDatabaseList.add(monsterDatabase2);
        mockMonsterDatabaseList.add(monsterDatabase3);

        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(monster1));
        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(monster2));
        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(monster3));
    }

    @Test
    void addMonsterCollection() {
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
        assertThat(responseDto.getMonsters().get(2).getMonsterImage())
                .isEqualTo(mockMonsterDatabaseList.get(2).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터 컬렉션 조회 성공");

        verify(monsterCollectionRepository).findAllByUser(testUser);
    }
}