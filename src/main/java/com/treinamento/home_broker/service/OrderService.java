package com.treinamento.home_broker.service;

import com.treinamento.home_broker.DTO.OrderCreateRequestDTO;
import com.treinamento.home_broker.domain.enums.OrderStatus;
import com.treinamento.home_broker.entities.Order;
import com.treinamento.home_broker.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void createOrder(OrderCreateRequestDTO dto){
        Order order = Order.builder()
                .userId(dto.getUserId())
                .stockId(dto.getStockId())
                .type(dto.getType())
                .status(OrderStatus.OPEN)
                .unitPrice(dto.getUnitPrice())
                .totalAmount(dto.getTotalAmount())
                .executedAmount(0)
                .remainingAmount(dto.getTotalAmount())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        orderRepository.save(order);
    }
}
