package com.treinamento.home_broker.service;

import com.treinamento.home_broker.DTO.OrderCreateRequestDTO;
import com.treinamento.home_broker.DTO.OrderCancelRequestDTO;
import com.treinamento.home_broker.domain.enums.OrderStatus;
import com.treinamento.home_broker.domain.enums.OrderType;
import com.treinamento.home_broker.entities.Order;
import com.treinamento.home_broker.entities.UserWallet;
import com.treinamento.home_broker.repositories.OrderRepository;
import com.treinamento.home_broker.repositories.UserWalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMatchingService orderMatchingService;
    private final UserWalletRepository userWalletRepository;

    public void createOrder(OrderCreateRequestDTO dto){

        UserWallet userWallet = userWalletRepository.findByUserIdAndStockId(dto.getUserId(), dto.getStockId());

        if(dto.getType() == OrderType.SELL){
            if(userWallet == null || userWallet.getAvailableAmount() < dto.getTotalAmount()){
                throw new RuntimeException("Usuario sem saldo para essa ação");
            }
            userWallet.setAvailableAmount(userWallet.getAvailableAmount() - dto.getTotalAmount());
            userWallet.setReservedAmount(userWallet.getReservedAmount() + dto.getTotalAmount());
            userWalletRepository.save(userWallet);
        }

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
        orderMatchingService.matchOrder(order);
    }
    @Transactional
    public void cancelOrder(UUID orderId){
        Order userOrder = orderRepository.findOrderById(orderId);
        if (userOrder.getStatus() == OrderStatus.CANCELED || userOrder.getStatus() == OrderStatus.EXECUTED) {
            throw new IllegalStateException("Status atual não permite cancelamento");
        }
        int remainingAmount = userOrder.getRemainingAmount();
        if (userOrder.getStatus() == OrderStatus.OPEN || userOrder.getStatus() == OrderStatus.PARTIAL) {
            if (userOrder.getType() != OrderType.BUY) {
                   UserWallet userWallet = userWalletRepository.findByUserIdAndStockId(userOrder.getUserId(), userOrder.getStockId());
                   if (userWallet.getReservedAmount() < remainingAmount) {
                       throw new IllegalStateException("Reserva inconsistente");
                   }
                    userWallet.setReservedAmount(userWallet.getReservedAmount() - remainingAmount);
                    userWallet.setAvailableAmount(userWallet.getAvailableAmount() + remainingAmount);
                    userWalletRepository.save(userWallet);
                }
            }
            userOrder.setStatus(OrderStatus.CANCELED);
            userOrder.setUpdatedAt(Instant.now());
            orderRepository.save(userOrder);
    }
}
