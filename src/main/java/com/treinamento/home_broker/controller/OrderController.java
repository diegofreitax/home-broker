package com.treinamento.home_broker.controller;

import com.treinamento.home_broker.DTO.OrderCreateRequestDTO;
import com.treinamento.home_broker.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
