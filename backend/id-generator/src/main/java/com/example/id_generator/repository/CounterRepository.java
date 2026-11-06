package com.example.id_generator.repository;

import com.example.id_generator.entity.CounterEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CounterRepository
        extends JpaRepository<CounterEntity, Long> {

    boolean existsByCounterKeyAndScopeKey(
            String counterKey,
            String scopeKey
    );

    Optional<CounterEntity>
    findByCounterKeyAndScopeKey(
            String counterKey,
            String scopeKey
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select c
            from CounterEntity c
            where c.counterKey = :counterKey
              and c.scopeKey = :scopeKey
            """)
    Optional<CounterEntity> findForUpdate(
            @Param("counterKey") String counterKey,
            @Param("scopeKey") String scopeKey
    );
}