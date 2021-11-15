package com.sollertia.habit.domain.notice.repository;

import com.sollertia.habit.domain.notice.entiy.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
