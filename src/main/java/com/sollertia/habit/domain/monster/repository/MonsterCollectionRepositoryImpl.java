package com.sollertia.habit.domain.monster.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.sollertia.habit.domain.monster.entity.QMonsterCollection.monsterCollection;
import static com.sollertia.habit.domain.monster.entity.QMonsterCollectionDatabase.monsterCollectionDatabase;
import static com.sollertia.habit.domain.monster.entity.QMonsterDatabase.monsterDatabase;

@RequiredArgsConstructor
public class MonsterCollectionRepositoryImpl implements MonsterCollectionRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MonsterCollection> searchByUser(User user) {
        return queryFactory
                .selectFrom(monsterCollection)
                .distinct()
                .join(monsterCollection.monsterCollectionDatabaseList, monsterCollectionDatabase).fetchJoin()
                .join(monsterCollectionDatabase.monsterDatabase, monsterDatabase).fetchJoin()
                .where(monsterCollection.user.eq(user))
                .fetch();
    }

    @Override
    public List<MonsterType> searchTypeListByUser(User user) {
        return queryFactory
                .select(monsterCollection.monsterType)
                .from(monsterCollection)
                .distinct()
                .where(monsterCollection.user.eq(user))
                .fetch();
    }
}
