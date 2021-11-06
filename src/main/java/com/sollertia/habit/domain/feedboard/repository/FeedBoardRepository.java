package com.sollertia.habit.domain.feedboard.repository;

import com.sollertia.habit.domain.feedboard.entity.FeedBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedBoardRepository extends JpaRepository<FeedBoard, Long> {
}
