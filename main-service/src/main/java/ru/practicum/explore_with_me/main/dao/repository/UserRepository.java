package ru.practicum.explore_with_me.main.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explore_with_me.main.dao.entity.UserEntity;

import java.util.Collection;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
        PagingAndSortingRepository<UserEntity, Long> {

    boolean existsByEmail(String email);

    Page<UserEntity> findAllByIdIn(Collection<Long> ids, Pageable pageable);
}
