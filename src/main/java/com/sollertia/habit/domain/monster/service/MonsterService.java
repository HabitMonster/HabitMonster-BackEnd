package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterDatabase;
import com.sollertia.habit.domain.monster.entity.MonsterType;
import com.sollertia.habit.domain.monster.enums.Level;
import com.sollertia.habit.domain.monster.repository.MonsterDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterRepository;
import com.sollertia.habit.domain.user.entity.User;
import com.sollertia.habit.domain.user.service.UserService;
import com.sollertia.habit.global.exception.monster.MonsterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final MonsterDatabaseRepository monsterDatabaseRepository;
    private final UserService userService;
    private final MonsterCollectionService monsterCollectionService;

    public MonsterListResponseDto getAllMonsters(User user) {
        List<MonsterDatabase> monsterDatabases = monsterDatabaseRepository.findAllByLevel(Level.LV1);
        return MonsterListResponseDto.builder()
                .monsters(MonsterSummaryVo.listFromMonsterList(monsterDatabases))
                .responseMessage("LV1 Monster Query Completed")
                .statusCode(200)
                .build();
    }

    @Transactional
    public MonsterResponseDto updateMonster(User user,
                                            MonsterSelectRequestDto requestDto) {
        String monsterName = requestDto.getMonsterName();
        MonsterDatabase monsterDatabase = getMonsterDatabaseById(requestDto.getMonsterId());

        Monster newMonster = Monster.createNewMonster(monsterName, monsterDatabase);
        User updatedUser = userService.updateMonster(user, newMonster);
        monsterCollectionService.addMonsterCollection(updatedUser.getMonster());

        MonsterVo monsterVo = MonsterVo.of(updatedUser.getMonster());

        return MonsterResponseDto.builder()
                .monster(monsterVo)
                .responseMessage("Selected Monster")
                .statusCode(200)
                .build();
    }

    @Transactional
    public MonsterResponseDto updateMonsterName(User user,
                                            MonsterSelectRequestDto requestDto) {
        Monster monster = getMonsterByUser(user);
        monster = monster.updateName(requestDto.getMonsterName());
        MonsterVo monsterVo = MonsterVo.of(monster);

        return MonsterResponseDto.builder()
                .monster(monsterVo)
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

    private MonsterVo getMonsterVo(User user) {
        Monster monster = getMonsterByUser(user);
        return MonsterVo.of(monster);
    }

    @Transactional
    public void plusExpPoint(User user) {
        Monster monster = getMonsterByUser(user);
        boolean isLevelUp = monster.plusExpPoint();
        if ( isLevelUp ) {
            Level level = monster.levelUp();
            evoluteMonster(monster, level);
            monsterCollectionService.addEvolutedMonster(user);
        }
    }

    private void evoluteMonster(Monster monster, Level level) {
        MonsterType monsterType = monster.getMonsterDatabase().getMonsterType();
        MonsterDatabase monsterDatabase = monsterDatabaseRepository.findByMonsterTypeAndLevel(monsterType, level)
                .orElseThrow( () -> new MonsterNotFoundException("NotFound Monster Database"));
        monster.updateMonsterDatabase(monsterDatabase);
    }

    private MonsterDatabase getMonsterDatabaseById(Long id) {
        return monsterDatabaseRepository.findById(id).orElseThrow(
                () -> new MonsterNotFoundException("NotFound Monster Id")
        );
    }

    private Monster getMonsterByUser(User user) {
        return monsterRepository.findByUserId(user.getId()).orElseThrow(
                () -> new MonsterNotFoundException("NotFound Selected Monster from User"));
    }
}
