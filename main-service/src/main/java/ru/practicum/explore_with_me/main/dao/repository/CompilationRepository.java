package ru.practicum.explore_with_me.main.dao.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.main.dao.entity.CompilationEntity;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<CompilationEntity, Long>,
        PagingAndSortingRepository<CompilationEntity, Long> {

    List<CompilationEntity> findAllByPinned(boolean pinned, Pageable pageable);
}
