package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.user.Level;
import com.sollertia.habit.domain.user.User;
import com.sollertia.habit.domain.user.UserRepository;
import com.sollertia.habit.exception.MonsterNotFoundException;
import com.sollertia.habit.exception.NotReachedMaximumLevelException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final MonsterCollectionRepository monsterCollectionRepository;
    private final UserRepository userRepository;

    public MonsterListResponseDto getAllMonsters(User user) {
        List<Monster> monsters = monsterRepository.findAllByGrade(EvolutionGrade.EV1);
        if ( user.getMonster() != null ) {
            List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
            for (MonsterCollection monsterCollection : monsterCollectionList) {
                monsters.remove(monsterCollection.getMonster());
            }
            monsters.remove(user.getMonster());
        }
        return MonsterListResponseDto.builder()
                .monsters(MonsterSummaryVo.listFromMonsterList(monsters))
                .responseMessage("LV1 몬스터를 불러오는데 성공했습니다.")
                .statusCode(200)
                .build();
    }

    public MonsterResponseDto updateMonster(User user,
                                            MonsterSelectRequestDto requestDto) {
        Monster monster = monsterRepository.findById(requestDto.getMonsterId()).orElseThrow(
                () -> new MonsterNotFoundException("올바르지 않은 몬스터 ID입니다.")
        );
        String monsterName = requestDto.getMonsterName();

        if ( user.getMonster() != null ) {
            addMonsterCollection(user, monster);
        }
        user.updateMonster(monster, monsterName);
        userRepository.save(user);

        MonsterVo monsterVo = MonsterVo.of(monster, monsterName);

        return MonsterResponseDto.builder()
                .monster(monsterVo)
                .responseMessage("몬스터가 선택되었습니다.")
                .statusCode(200)
                .build();
    }

    public MonsterListResponseDto getMonsterCollection(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
        return MonsterListResponseDto.builder()
                .monsters(MonsterSummaryVo.listFromCollectionList(monsterCollectionList))
                .responseMessage("몬스터 컬렉션 조회 성공")
                .statusCode(200)
                .build();
    }

    private void addMonsterCollection(User user, Monster monster) {
//        if (user.getLevel().getValue() == Level.MAX_LEVEL) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(user, monster);
        monsterCollectionRepository.save(monsterCollection);
//        } else {
//            throw new NotReachedMaximumLevelException("최대 레벨에 도달하지 못했습니다.");
//        }
    }

    public MonsterVo getMonsterVo(User user) {
       Monster monster = monsterRepository.findById(user.getMonster().getId()).orElseThrow(
                () -> new MonsterNotFoundException("올바르지 않은 몬스터 ID입니다.")
        );
        return MonsterVo.of(monster, user.getMonsterName());
    }
}
