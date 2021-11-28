package com.sollertia.habit.domain.monster.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.global.globaldto.SearchDateDto;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sollertia.habit.domain.monster.entity.QMonster.monster;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;

@RequiredArgsConstructor
public class MonsterRepositoryImpl implements MonsterRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Map<MonsterType, Long> getMonsterTypeCount(SearchDateDto searchDateDto) {

        List<Tuple> result = jpaQueryFactory
                .select(monsterDatabase.monsterType, monsterDatabase.id.count())
                .from(monster)
                .join(monster.monsterDatabase, monsterDatabase)
                .where(monster.createdAt.between(searchDateDto.getSearchStartDate(), searchDateDto.getSearchEndDate()))
                .groupBy(monsterDatabase.monsterType)
                .fetch();

        return resultToHashMap(result);
    }

    private Map<MonsterType, Long> resultToHashMap(List<Tuple> result) {
        Map<MonsterType, Long> resultMap = new HashMap<>();
        for (Tuple tuple : result) {
            resultMap.put(tuple.get(0, MonsterType.class), tuple.get(1, Long.class));
        }
        return resultMap;
    }
}
