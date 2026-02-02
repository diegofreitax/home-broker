package com.treinamento.home_broker.controller;

import com.treinamento.home_broker.DTO.OrderCreateRequestDTO;
import com.treinamento.home_broker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<Void> createOrder(@RequestBody OrderCreateRequestDTO request) {
        orderService.createOrder(request);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/orders/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok().build();
    }

}
