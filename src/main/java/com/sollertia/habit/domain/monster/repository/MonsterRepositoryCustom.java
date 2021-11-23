package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.global.globaldto.SearchDateDto;

import java.util.Map;

public interface MonsterRepositoryCustom {


    public Map<String, Integer> getMonsterTypeCount(SearchDateDto searchDateDto);

}
