package com.cayetano.guesscard.repository;

import com.cayetano.guesscard.model.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity, Long> {
    Optional<CardEntity> findByOwnerId(Long id);

    Optional<CardEntity> findByCardNumber(String cardNumber);
}
