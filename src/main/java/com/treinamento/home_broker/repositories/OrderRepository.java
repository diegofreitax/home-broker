package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.Order;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
