package com.dqt.positionservice.repository;

import com.dqt.positionservice.model.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Long> {
    Page<Position> findByNameContaining(String name, Pageable page);

}
