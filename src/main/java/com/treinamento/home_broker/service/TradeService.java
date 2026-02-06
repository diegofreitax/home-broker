package com.treinamento.home_broker.service;

import com.treinamento.home_broker.domain.enums.OrderStatus;
import com.treinamento.home_broker.domain.enums.OrderType;
import com.treinamento.home_broker.entities.Order;
import com.treinamento.home_broker.entities.Trade;
import com.treinamento.home_broker.entities.UserWallet;
import com.treinamento.home_broker.repositories.OrderRepository;
import com.treinamento.home_broker.repositories.TradesRepository;
import com.treinamento.home_broker.repositories.UserWalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TradeService {


    private final OrderRepository orderRepository;
    private final TradesRepository tradesRepository;
    private final UserWalletRepository userWalletRepository;
    private final UserBalanceService userBalanceService;

    @Transactional
    public void matchOrder(Order orderReceived) {

        List<Order> orders;

        if (orderReceived.getType() == OrderType.BUY) {
            orders = orderRepository.findSellOrdersToMatch(orderReceived.getStockId());
        } else {
            orders = orderRepository.findBuyOrdersToMatch(orderReceived.getStockId());
        }

        for (Order order : orders) {
            boolean priceMatch;
            if (orderReceived.getType() == OrderType.BUY) {
                priceMatch = order.getUnitPrice().compareTo(orderReceived.getUnitPrice()) <= 0;
            } else {
                priceMatch = order.getUnitPrice().compareTo(orderReceived.getUnitPrice()) >= 0;
            }
            if (!priceMatch) {
                break;
            } else {

                int executedAmount = Math.min(orderReceived.getRemainingAmount(), order.getRemainingAmount());

                orderReceived.setRemainingAmount(orderReceived.getRemainingAmount() - executedAmount);
                order.setRemainingAmount(order.getRemainingAmount() - executedAmount);

                orderReceived.setExecutedAmount(orderReceived.getExecutedAmount() + executedAmount);
                order.setExecutedAmount(order.getExecutedAmount() + executedAmount);

                orderReceived.setStatus(orderReceived.getRemainingAmount() == 0 ? OrderStatus.EXECUTED : OrderStatus.PARTIAL);
                order.setStatus(order.getRemainingAmount() == 0 ? OrderStatus.EXECUTED : OrderStatus.PARTIAL);

                Trade trade = new Trade(
                        null,
                        orderReceived.getType() == OrderType.BUY ? orderReceived.getId() : order.getId(),
                        orderReceived.getType() == OrderType.SELL ? orderReceived.getId() : order.getId(),
                        orderReceived.getStockId(),
                        executedAmount,
                        order.getUnitPrice(),
                        Instant.now()
                );

                tradesRepository.save(trade);
                orderRepository.save(order);
                orderRepository.save(orderReceived);

                Order buyOrder = orderRepository.findById(trade.getBuyOrderId()).orElseThrow();
                Long buyerOrderId = buyOrder.getUserId();

                Order sellOrder = orderRepository.findById(trade.getSellOrderId()).orElseThrow();
                Long sellerOrderId = sellOrder.getUserId();

                UserWallet walletBuyer = userWalletRepository.findByUserIdAndStockId(buyerOrderId, trade.getStockId());
                if (walletBuyer == null) {
                    walletBuyer = new UserWallet(
                            null,
                            buyerOrderId,
                            trade.getStockId(),
                            trade.getQuantity(),
                            0
                    );
                } else {
                   walletBuyer.setAvailableAmount((walletBuyer.getAvailableAmount() + trade.getQuantity()));
                }
                userWalletRepository.save(walletBuyer);

                UserWallet walletSeller = userWalletRepository.findByUserIdAndStockId(sellerOrderId, trade.getStockId());

                int reserved = walletSeller.getReservedAmount();
                int executed = trade.getQuantity();

                if (reserved < executed){
                    throw new IllegalStateException("Valor reservado no balance insuficiente para executar");
                }
                walletSeller.setReservedAmount(reserved - executed);
                userWalletRepository.save(walletSeller);

                BigDecimal tradeValue = trade.getPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));
                userBalanceService.debitReservedBalance(buyOrder.getUserId(), tradeValue);
                userBalanceService.creditAvailableBalance(sellOrder.getUserId(), tradeValue);

                if (orderReceived.getRemainingAmount() == 0) {
                    break;
                }
            }
        }
    }
}