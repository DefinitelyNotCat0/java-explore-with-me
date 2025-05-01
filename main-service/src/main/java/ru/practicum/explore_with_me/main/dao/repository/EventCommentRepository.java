package ru.practicum.explore_with_me.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.main.dao.entity.EventCommentEntity;

import java.util.List;

@Repository
public interface EventCommentRepository extends JpaRepository<EventCommentEntity, Long>,
        PagingAndSortingRepository<EventCommentEntity, Long> {

    List<EventCommentEntity> findAllByEventId(Long eventId);
}
