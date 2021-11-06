package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.Monster;
import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    Optional<Monster> findByUser(User user);
}
