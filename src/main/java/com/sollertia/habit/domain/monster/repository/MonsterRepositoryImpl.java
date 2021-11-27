package com.sollertia.habit.domain.monster.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.monster.dto.MonsterTypeCountDto;
import com.sollertia.habit.domain.monster.entity.QMonster;
import com.sollertia.habit.domain.monster.entity.QMonsterDatabase;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import io.swagger.models.auth.In;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sollertia.habit.domain.monster.entity.QMonster.*;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.*;

@RequiredArgsConstructor
public class MonsterRepositoryImpl implements MonsterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<String, Integer> getMonsterTypeCount(SearchDateDto searchDateDto) {

        List<Tuple> result = jpaQueryFactory
                .select(monsterDatabase.monsterType, monsterDatabase.id.count())
                .join(monster.monsterDatabase, monsterDatabase)
                .on(monster.id.eq(monsterDatabase.id))
                .where(monster.createdAt.between(searchDateDto.getSearchStartDate(), searchDateDto.getSearchEndDate()))
                .groupBy(monsterDatabase.monsterType)
                .fetch();

        return resultToHashMap(result);
    }

    private Map<String, Integer> resultToHashMap(List<Tuple> result) {
        Map<String, Integer> resultMap = new HashMap<>();
        for (Tuple tuple : result) {
            resultMap.put(tuple.get(0, String.class), tuple.get(1, Integer.class));
        }
        return resultMap;
    }
}
