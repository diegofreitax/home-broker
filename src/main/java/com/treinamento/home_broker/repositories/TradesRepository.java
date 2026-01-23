package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TradesRepository extends JpaRepository<Trade, UUID> {
}
