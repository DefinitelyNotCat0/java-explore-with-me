package ru.practicum.explore_with_me.main.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.main.dao.entity.EventEntity;
import ru.practicum.explore_with_me.main.dto.event.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long>,
        PagingAndSortingRepository<EventEntity, Long>,
        JpaSpecificationExecutor<EventEntity> {

    boolean existsByIdAndInitiator_Id(Long id, Long userId);

    boolean existsByCategory_Id(Long categoryId);

    Optional<EventEntity> findByIdAndState(Long id, EventState eventState);

    List<EventEntity> findAllByInitiator_Id(Long id, Pageable pageable);

    Optional<EventEntity> findByIdAndInitiator_Id(Long userId, Long id);

    Set<EventEntity> findAllByIdIn(Set<Long> events);
}
