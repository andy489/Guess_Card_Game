package com.cayetano.guesscard.repository;

import com.cayetano.guesscard.model.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<GameEntity, Long> {
    Optional<GameEntity> findByOwnerId(Long id);
}
