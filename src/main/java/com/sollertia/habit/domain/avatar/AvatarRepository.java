package com.sollertia.habit.domain.avatar;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
    List<Avatar> findAllByGrade(EvolutionGrade ev1);
}
