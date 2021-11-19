package com.sollertia.habit.domain.monster.repository;

import com.sollertia.habit.domain.monster.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    @Query("select m from Monster m " +
            "join fetch m.user " +
            "where m.user.id = :userId")
    Optional<Monster> findByUserId(@Param("userId") Long userId);
}
