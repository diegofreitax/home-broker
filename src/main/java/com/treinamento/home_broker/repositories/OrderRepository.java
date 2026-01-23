package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.domain.enums.OrderStatus;
import com.treinamento.home_broker.domain.enums.OrderType;
import com.treinamento.home_broker.entities.Order;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT o FROM Order o
        WHERE o.stockId = :stockId
          AND o.type = 'SELL'
          AND o.status IN ('OPEN', 'PARTIAL')
          AND o.remainingAmount > 0
        ORDER BY o.unitPrice ASC, o.createdAt ASC
    """)
    List<Order> findSellOrdersToMatch(Long stockId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT o FROM Order o
        WHERE o.stockId = :stockId
          AND o.type = 'BUY'
          AND o.status IN ('OPEN', 'PARTIAL')
          AND o.remainingAmount > 0
        ORDER BY o.unitPrice DESC, o.createdAt ASC
    """)
    List<Order> findBuyOrdersToMatch(Long stockId);
}
