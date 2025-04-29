package ru.practicum.explore_with_me.main.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.main.dao.entity.EventRequestEntity;
import ru.practicum.explore_with_me.main.dto.eventrequest.EventRequestStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRequestRepository extends JpaRepository<EventRequestEntity, Long>,
        PagingAndSortingRepository<EventRequestEntity, Long>,
        JpaSpecificationExecutor<EventRequestEntity> {

    List<EventRequestEntity> findAllByEvent_Id(Long eventId);

    Integer countByEvent_IdAndStatus(Long eventId, EventRequestStatus eventRequestStatus);

    @Query("select e.id, count(e.id) " +
            "from EventRequestEntity r " +
            "inner join EventEntity e " +
            "where e.id in (:ids) " +
            " and r.status = :requestStatus " +
            "group by e.id ")
    Map<Long, Integer> countByEvent_IdInAndStatus(Set<Long> ids, EventRequestStatus requestStatus);


    List<EventRequestEntity> findAllByRequester_Id(Long requesterId);

    boolean existsByEvent_IdAndRequester_Id(Long eventId, Long requesterId);

    Optional<EventRequestEntity> findByIdAndRequester_Id(Long id, Long requesterId);
}
