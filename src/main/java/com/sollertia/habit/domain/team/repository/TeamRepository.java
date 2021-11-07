package com.sollertia.habit.domain.team.repository;


import com.sollertia.habit.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
