package com.example.id_generator.repository;

import com.example.id_generator.entity.IssuedIdEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssuedIdRepository
        extends JpaRepository<IssuedIdEntity, Long> {

    List<IssuedIdEntity>
    findAllByOrderByCreatedAtDesc(Pageable pageable);
}