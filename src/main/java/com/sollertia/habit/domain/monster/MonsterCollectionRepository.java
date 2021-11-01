package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterCollectionRepository extends JpaRepository<MonsterCollection, Long> {
    List<MonsterCollection> findAllByUser(User user);
}
