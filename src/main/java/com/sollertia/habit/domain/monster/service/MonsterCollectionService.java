package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterCollectionVo;
import com.sollertia.habit.domain.monster.dto.MonsterDatabaseVo;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterCollectionService {

    private final MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;
    private final MonsterCollectionRepository monsterCollectionRepository;
    private final MonsterDatabaseRepository monsterDatabaseRepository;

    @Transactional
    public MonsterCollection addMonsterCollection(Monster monster) {
        //        if (user.getLevel().getValue() == Level.MAX_LEVEL) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(monster);
        MonsterCollectionDatabase monsterCollectionDatabase =
                MonsterCollectionDatabase.from(monster.getMonsterDatabase(), monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);
        return monsterCollection;
//        } else {
//            throw new NotReachedMaximumLevelException("최대 레벨에 도달하지 못했습니다.");
//        }
    }

    @Transactional
    public MonsterCollection addEvolutedMonster(User user) {
        MonsterType monsterType = user.getMonster().getMonsterDatabase().getMonsterType();
        MonsterCollection monsterCollection = monsterCollectionRepository.findByUserAndMonsterType(user, monsterType);
        MonsterCollectionDatabase monsterCollectionDatabase =
                MonsterCollectionDatabase.from(user.getMonster().getMonsterDatabase(), monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);
        return monsterCollection;
    }

    @Transactional
    public MonsterCollectionResponseDto getMonsterCollection(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
        List<MonsterCollectionVo> monsterCollectionVoList = new ArrayList<>();

        for (MonsterCollection monsterCollection : monsterCollectionList) {
            List<MonsterDatabaseVo> monsterDatabaseVoList = new ArrayList<>();
            List<MonsterCollectionDatabase> monsterCollectionDatabaseList =
                    monsterCollection.getMonsterCollectionDatabaseList();
            for (MonsterCollectionDatabase monsterCollectionDatabase : monsterCollectionDatabaseList) {
                monsterDatabaseVoList.add(MonsterDatabaseVo.of(monsterCollectionDatabase.getMonsterDatabase()));
            }
            monsterCollectionVoList.add(MonsterCollectionVo.of(monsterCollection, monsterDatabaseVoList));
        }

        if ( monsterCollectionVoList.size() == 1 ) {
            if ( monsterCollectionVoList.get(0).getMonsterDatabases().size() == 1 ) {
                monsterCollectionVoList = null;
            }
        }

        return MonsterCollectionResponseDto.builder()
                .monsters(monsterCollectionVoList)
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200)
                .build();
    }


}
