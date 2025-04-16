package ru.practicum.explore_with_me.stat.server.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.stat.server.dao.entity.HitEntity;
import ru.practicum.explore_with_me.stat.server.dao.model.AppUriHitCountEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HitRepository extends JpaRepository<HitEntity, Long> {

    @Query(
            value = "select h.app, h.uri, count(distinct h.ip) as hits " +
                    "from hits h " +
                    "where h.creation_date >= :start and h.creation_date <= :end " +
                    "group by h.app, h.uri " +
                    "order by hits desc",
            nativeQuery = true
    )
    List<AppUriHitCountEntity> getStatsByDatesUnique(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "select h.app, h.uri, count(h.ip) as hits " +
                    "from hits h " +
                    "where h.creation_date >= :start and h.creation_date <= :end " +
                    "group by h.app, h.uri " +
                    "order by hits desc",
            nativeQuery = true
    )
    List<AppUriHitCountEntity> getStatsByDates(LocalDateTime start, LocalDateTime end);

    @Query(
            value = "select h.app, h.uri, count(distinct h.ip) as hits " +
                    "from hits h " +
                    "where h.creation_date >= :start and h.creation_date <= :end " +
                    "and h.uri in (:uriList) " +
                    "group by h.app, h.uri " +
                    "order by hits desc",
            nativeQuery = true
    )
    List<AppUriHitCountEntity> getStatsByDatesAndUriUnique(LocalDateTime start, LocalDateTime end, List<String> uriList);

    @Query(
            value = "select h.app, h.uri, count(h.ip) as hits " +
                    "from hits h " +
                    "where h.creation_date >= :start and h.creation_date <= :end " +
                    "and h.uri in (:uriList) " +
                    "group by h.app, h.uri " +
                    "order by hits desc",
            nativeQuery = true
    )
    List<AppUriHitCountEntity> getStatsByDatesAndUri(LocalDateTime start, LocalDateTime end, List<String> uriList);
}
