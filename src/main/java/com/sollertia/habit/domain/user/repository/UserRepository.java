package com.sollertia.habit.domain.user.repository;

import com.sollertia.habit.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

        Optional<User> findBySocialId(String socialId);

        Optional<User> findByMonsterCode(String monsterCode);
}
