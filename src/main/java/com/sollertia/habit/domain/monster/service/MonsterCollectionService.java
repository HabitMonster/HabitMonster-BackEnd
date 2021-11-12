package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.MonsterCollectionResponseDto;
import com.sollertia.habit.domain.monster.dto.MonsterVo;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.entity.MonsterCollectionDatabase;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionDatabaseRepository;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterCollectionService {

    private final MonsterCollectionDatabaseRepository monsterCollectionDatabaseRepository;
    private final MonsterCollectionRepository monsterCollectionRepository;

    @Transactional
    public MonsterCollection addMonsterCollection(Monster monster) {
        //        if (user.getLevel().getValue() == Level.MAX_LEVEL) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(monster);
        MonsterCollectionDatabase monsterCollectionDatabase =
                new MonsterCollectionDatabase(monster.getMonsterDatabase(), monsterCollection);
        monsterCollectionDatabaseRepository.save(monsterCollectionDatabase);
        return monsterCollection;
//        } else {
//            throw new NotReachedMaximumLevelException("최대 레벨에 도달하지 못했습니다.");
//        }
    }

    public MonsterCollectionResponseDto getMonsterCollection(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
        if ( monsterCollectionList.size() <= 1 ) {
            monsterCollectionList.clear();
        }
        return MonsterCollectionResponseDto.builder()
                .monsters(MonsterVo.listOf(monsterCollectionList))
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200)
                .build();
    }
}
