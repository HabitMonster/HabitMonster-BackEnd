package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterCollectionDto;
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
        List<MonsterCollectionDto> monsterCollectionDtoList = getMonsterCollectionDtoListByUser(user);

        return MonsterCollectionResponseDto.builder()
                .monsters(monsterCollectionDtoList)
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200)
                .build();
    }

    private List<MonsterCollectionDto> getMonsterCollectionDtoListByUser(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.searchByUser(user);
        List<MonsterCollectionDto> monsterCollectionDtoList = MonsterCollectionDto.listOf(monsterCollectionList);

        if ( onlyOneMonsterExist(monsterCollectionDtoList) ) {
            monsterCollectionDtoList = null;
        }

        return monsterCollectionDtoList;
    }

    private boolean onlyOneMonsterExist(List<MonsterCollectionDto> monsterCollectionDtoList) {
        return monsterCollectionDtoList.size() == 1
                && monsterCollectionDtoList.get(0).getMonsterDatabases().size() == 1;
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

    public List<MonsterType> getMonsterTypeListByUser(User user) {
        return monsterCollectionRepository.searchTypeListByUser(user);
    }
}
