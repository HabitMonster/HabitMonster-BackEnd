package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterCollectionVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterCollectionService {

    private final MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;
    private final MonsterCollectionRepository monsterCollectionRepository;

    @Transactional
    public MonsterCollection addMonsterCollection(Monster monster) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(monster);
        monsterCollection = monsterCollectionRepository.save(monsterCollection);
        createMonsterCollectionDatabase(monster, monsterCollection);
        return monsterCollection;
    }

    @Transactional
    public MonsterCollection addEvolutedMonster(User user) {
        MonsterCollection monsterCollection = getCurrentMonsterCollectionByUser(user);
        monsterCollection.updateMaxLevel(user.getMonster().getLevel());
        createMonsterCollectionDatabase(user.getMonster(), monsterCollection);
        return monsterCollection;
    }

    private void createMonsterCollectionDatabase(Monster monster, MonsterCollection monsterCollection) {
        MonsterCollectionDatabase monsterCollectionDatabase =
                MonsterCollectionDatabase.from(monster.getMonsterDatabase(), monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);
    }

    @Transactional
    public MonsterCollectionResponseDto getMonsterCollectionResponseDto(User user) {
        List<MonsterCollection> monsterCollectionList = getMonsterCollectionListByUser(user);
        List<MonsterCollectionVo> monsterCollectionVoList = getMonsterCollectionVoListFrom(monsterCollectionList);

        return MonsterCollectionResponseDto.builder()
                .monsters(monsterCollectionVoList)
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200)
                .build();
    }

    public List<MonsterCollection> getMonsterCollectionListByUser(User user) {
        return monsterCollectionRepository.findAllByUser(user);
    }

    private List<MonsterCollectionVo> getMonsterCollectionVoListFrom(List<MonsterCollection> monsterCollectionList) {
        List<MonsterCollectionVo> monsterCollectionVoList = MonsterCollectionVo.listOf(monsterCollectionList);

        if ( onlyOneMonsterExist(monsterCollectionVoList) ) {
            monsterCollectionVoList = null;
        }

        return monsterCollectionVoList;
    }

    private boolean onlyOneMonsterExist(List<MonsterCollectionVo> monsterCollectionVoList) {
        return monsterCollectionVoList.size() == 1 && monsterCollectionVoList.get(0).getMonsterDatabases().size() == 1;
    }

    @Transactional
    public void updateMonsterName(User user, String monsterName) {
        MonsterCollection monsterCollection = getCurrentMonsterCollectionByUser(user);
        monsterCollection.updateMonsterName(monsterName);
    }

    private MonsterCollection getCurrentMonsterCollectionByUser(User user) {
        MonsterType monsterType = user.getMonster().getMonsterDatabase().getMonsterType();
        return monsterCollectionRepository.findByUserAndMonsterType(user, monsterType);
    }
}
