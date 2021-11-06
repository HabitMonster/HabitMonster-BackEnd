package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.MonsterCollection;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterCollectionRepository extends JpaRepository<MonsterCollection, Long> {
    List<MonsterCollection> findAllByUser(User user);
}
