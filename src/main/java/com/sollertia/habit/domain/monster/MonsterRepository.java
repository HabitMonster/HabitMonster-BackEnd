package com.sollertia.habit.domain.monster;

import com.sollertia.habit.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MonsterRepository extends JpaRepository<Monster, Long> {
    Optional<Monster> findByUser(User user);
}
