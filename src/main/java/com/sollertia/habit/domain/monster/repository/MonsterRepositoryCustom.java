package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.enums.MonsterType;
import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.util.Map;

public interface MonsterRepositoryCustom {


    public Map<MonsterType, Long> getMonsterTypeCount(SearchDateDto searchDateDto);

}
