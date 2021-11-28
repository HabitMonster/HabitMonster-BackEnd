package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import com.sollertia.habit.global.exception.monster.NotReachedMaximumLevelException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MonsterService {
    private final MonsterRepository monsterRepository;

    private final MonsterDatabaseRepository monsterDatabaseRepository;
    private final MonsterCollectionService monsterCollectionService;

    public MonsterListResponseDto getAllMonsters(User user) {
        List<MonsterType> monsterTypeList = monsterCollectionService.getMonsterTypeListByUser(user);
        List<MonsterDatabase> monsterDatabaseList = monsterDatabaseRepository.findAllByLevel(Level.LV1);

        return MonsterListResponseDto.builder()
                .monsters(MonsterSummaryDto.listFromMonsterDatabasesDisabledIfNotIn
                        (monsterDatabaseList, monsterTypeList))
                .responseMessage("LV1 Monster Query Completed")
                .statusCode(200)
                .build();
    }

    @Transactional
    public MonsterResponseDto updateMonster(User user,
                                            MonsterSelectRequestDto requestDto) {
        Monster currentMonster = user.getMonster();
        if ( currentMonster == null || currentMonster.changeable()) {
            Monster newMonster = getNewMonster(requestDto);
            newMonster = changeMonster(user, newMonster);

            MonsterDto monsterDto = MonsterDto.of(newMonster);
            return MonsterResponseDto.builder()
                    .monster(monsterDto)
                    .responseMessage("Selected Monster")
                    .statusCode(200)
                    .build();
        } else {
            throw new NotReachedMaximumLevelException("New monsters require max level and EXP.");
        }
    }

    private Monster getNewMonster(MonsterSelectRequestDto requestDto) {
        String monsterName = requestDto.getMonsterName();
        MonsterDatabase monsterDatabase = getMonsterDatabaseById(requestDto.getMonsterId());
        return Monster.createNewMonster(monsterName, monsterDatabase);
    }

    private Monster changeMonster(User user, Monster monster) {
        Monster savedMonster = monsterRepository.save(monster);
        user.updateMonster(savedMonster);
        monsterCollectionService.addMonsterCollection(savedMonster);
        return savedMonster;
    }

    @Transactional
    public MonsterResponseDto updateMonsterName(User user,
                                            MonsterSelectRequestDto requestDto) {
        Monster monster = getMonsterByUser(user);
        monster = monster.updateName(requestDto.getMonsterName());
        MonsterDto monsterDto = MonsterDto.of(monster);

        return MonsterResponseDto.builder()
                .monster(monsterDto)
                .responseMessage("Change Monster Name")
                .statusCode(200)
                .build();
    }

    public MonsterResponseDto getMonsterResponseDtoFromUser(User user) {
        return MonsterResponseDto.builder()
                .monster(getMonsterVo(user))
                .statusCode(200)
                .responseMessage("User Monster Query Completed")
                .build();
    }

    private MonsterDto getMonsterVo(User user) {
        Monster monster = getMonsterByUser(user);
        return MonsterDto.of(monster);
    }

    @Transactional
    public void plusExpPoint(User user) {
        Monster monster = getMonsterByUser(user);
        boolean isLevelUp = monster.plusExpPoint();
        if ( isLevelUp ) {
            evoluteMonster(monster);
            monsterCollectionService.addEvolutedMonster(user);
        }
    }

    private void evoluteMonster(Monster monster) {
        Level level = monster.levelUp();
        MonsterType monsterType = monster.getMonsterDatabase().getMonsterType();
        MonsterDatabase monsterDatabase = monsterDatabaseRepository.findByMonsterTypeAndLevel(monsterType, level)
                .orElseThrow( () -> new MonsterNotFoundException("Not Found Monster Database"));
        monster.updateMonsterDatabase(monsterDatabase);
    }

    private MonsterDatabase getMonsterDatabaseById(Long id) {
        return monsterDatabaseRepository.findById(id).orElseThrow(
                () -> new MonsterNotFoundException("Not Found Monster Id")
        );
    }

    @Transactional
    public void minusExpWithCount(User user, Long count) {
        Monster monster = getMonsterByUser(user);
        for (int i = 0; i < count; i++) {
            monster.minusExpPoint();
        }
    }

    private Monster getMonsterByUser(User user) {
        return monsterRepository.findByUserId(user.getId()).orElseThrow(
                () -> new MonsterNotFoundException("Not Found Selected Monster from User"));
    }
}
