package com.sollertia.habit.domain.monster.service;


import com.sollertia.habit.domain.monster.dto.*;
import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.repository.MonsterCollectionRepository;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonsterCollectionService {

    private final MonsterCollectionRepository monsterCollectionRepository;

    @Transactional
    public MonsterCollection addMonsterCollection(Monster monster) {
        //        if (user.getLevel().getValue() == Level.MAX_LEVEL) {
        MonsterCollection monsterCollection = MonsterCollection.createMonsterCollection(monster);
        return monsterCollectionRepository.save(monsterCollection);
//        } else {
//            throw new NotReachedMaximumLevelException("최대 레벨에 도달하지 못했습니다.");
//        }
    }

    public MonsterCollectionResponseDto getMonsterCollection(User user) {
        List<MonsterCollection> monsterCollectionList = monsterCollectionRepository.findAllByUser(user);
        return MonsterCollectionResponseDto.builder()
                .monsters(MonsterVo.listOf(monsterCollectionList))
                .responseMessage("Monster Collection Query Completed")
                .statusCode(200)
                .build();
    }
}
