package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.MonsterListResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterSelectRequestDto;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.oauth2.userinfo.GoogleOauth2UserInfo;
import com.sollertia.habit.domain.oauth2.userinfo.Oauth2UserInfo;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MonsterServiceTest {

    @InjectMocks
    private MonsterService monsterService;

    @Mock
    private MonsterRepository monsterRepository;
    @Mock
    private MonsterCollectionRepository monsterCollectionRepository;
    @Mock
    private UserRepository userRepository;

    User testUser;
    List<Monster> mockMonsterList = new ArrayList<>();
    List<MonsterCollection> mockMonsterCollectionList = new ArrayList<>();

    @BeforeEach
    private void beforeEach() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sub", "123456789");
        attributes.put("name", "tester");
        attributes.put("email", "tester.test.com");
        Oauth2UserInfo oauth2UserInfo = new GoogleOauth2UserInfo(attributes);
        testUser = User.create(oauth2UserInfo);

        mockMonsterList.add(new Monster(EvolutionGrade.EV1, "cat.img"));
        mockMonsterList.add(new Monster(EvolutionGrade.EV1, "dog.img"));
        mockMonsterList.add(new Monster(EvolutionGrade.EV1, "dug.img"));

        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(testUser, mockMonsterList.get(0)));
        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(testUser, mockMonsterList.get(1)));
        mockMonsterCollectionList.add(MonsterCollection.createMonsterCollection(testUser, mockMonsterList.get(2)));
    }

    @Test
    void getAllMonsters() {
        //given
        given(monsterRepository.findAllByGrade(EvolutionGrade.EV1))
                .willReturn(mockMonsterList);

        //when
        MonsterListResponseDto responseDto = monsterService.getAllMonsters(testUser);

        //then
        assertThat(responseDto.getMonsters().get(0).getMonsterImage())
                .isEqualTo(mockMonsterList.get(0).getImageUrl());
        assertThat(responseDto.getMonsters().get(1).getMonsterImage())
                .isEqualTo(mockMonsterList.get(1).getImageUrl());
        assertThat(responseDto.getMonsters().get(2).getMonsterImage())
                .isEqualTo(mockMonsterList.get(2).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("LV1 몬스터를 불러오는데 성공했습니다.");

        verify(monsterRepository).findAllByGrade(EvolutionGrade.EV1);
    }

    @Test
    void updateMonster() {
        //given
        given(monsterRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterList.get(0)));
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto(1L, "test");

        //when
        MonsterResponseDto responseDto = monsterService.updateMonster(testUser, mockRequestDto);

        //then
        assertThat(responseDto.getMonster().getMonsterImage())
                .isEqualTo(mockMonsterList.get(0).getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(mockRequestDto.getMonsterName());
        assertThat(testUser.getMonster()).isEqualTo(mockMonsterList.get(0));
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터가 선택되었습니다.");

        verify(monsterRepository).findById(1L);
        verify(userRepository).save(testUser);
    }

    @Test
    void updateMonsterIfUserHasMonster() {
        //given
        testUser.updateMonster(mockMonsterList.get(0), "before");
        testUser.levelUp();
        testUser.levelUp();
        testUser.levelUp();
        testUser.levelUp();
        given(monsterRepository.findById(1L))
                .willReturn(Optional.of(mockMonsterList.get(1)));
        MonsterSelectRequestDto mockRequestDto = new MonsterSelectRequestDto(1L, "after");

        //when
        MonsterResponseDto responseDto = monsterService.updateMonster(testUser, mockRequestDto);

        //then
        assertThat(responseDto.getMonster().getMonsterImage())
                .isEqualTo(mockMonsterList.get(1).getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(mockRequestDto.getMonsterName());
        assertThat(testUser.getMonster()).isEqualTo(mockMonsterList.get(1));
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터가 선택되었습니다.");

        verify(monsterRepository).findById(1L);
        verify(monsterCollectionRepository).save(any(MonsterCollection.class));
        verify(userRepository).save(testUser);
    }

    @Test
    void getMonsterCollection() {
        //given
        given(monsterCollectionRepository.findAllByUser(testUser))
                .willReturn(mockMonsterCollectionList);

        //when
        MonsterListResponseDto responseDto = monsterService.getMonsterCollection(testUser);

        //then
        assertThat(responseDto.getMonsters().get(0).getMonsterImage())
                .isEqualTo(mockMonsterList.get(0).getImageUrl());
        assertThat(responseDto.getMonsters().get(1).getMonsterImage())
                .isEqualTo(mockMonsterList.get(1).getImageUrl());
        assertThat(responseDto.getMonsters().get(2).getMonsterImage())
                .isEqualTo(mockMonsterList.get(2).getImageUrl());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("몬스터 컬렉션 조회 성공");

        verify(monsterCollectionRepository).findAllByUser(testUser);
    }

    @Test
    void getMonsterVo() {
        //given
        testUser.updateMonster(mockMonsterList.get(0), "test");
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(mockMonsterList.get(0)));

        //when
        MonsterVo monsterVo = monsterService.getMonsterVo(testUser);

        //then
        assertThat(monsterVo.getMonsterName()).isEqualTo(testUser.getMonsterName());
        assertThat(monsterVo.getMonsterImage())
                .isEqualTo(mockMonsterList.get(0).getImageUrl());

        verify(monsterRepository).findById(any());
    }

    @Test
    void getMonsterFromUser() {
        //given
        testUser.updateMonster(mockMonsterList.get(0), "test");
        given(monsterRepository.findById(any()))
                .willReturn(Optional.of(mockMonsterList.get(0)));

        //when
        MonsterResponseDto responseDto = monsterService.getMonsterFromUser(testUser);

        //then
        assertThat(responseDto.getMonster().getMonsterImage())
                .isEqualTo(mockMonsterList.get(0).getImageUrl());
        assertThat(responseDto.getMonster().getMonsterName())
                .isEqualTo(testUser.getMonsterName());
        assertThat(responseDto.getStatusCode()).isEqualTo(200);
        assertThat(responseDto.getResponseMessage()).isEqualTo("사용자 몬스터 조회 성공");

        verify(monsterRepository).findById(any());
    }
}