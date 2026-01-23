package com.treinamento.home_broker.service;

import com.treinamento.home_broker.domain.enums.OrderStatus;
import com.treinamento.home_broker.domain.enums.OrderType;
import com.treinamento.home_broker.entities.Order;
import com.treinamento.home_broker.entities.Trade;
import com.treinamento.home_broker.repositories.OrderRepository;
import com.treinamento.home_broker.repositories.TradesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderMatchingService {


    private final OrderRepository orderRepository;
    private final TradesRepository tradesRepository;

    @Transactional
    public void matchOrder(Order orderRecebida){
        List<Order> orders;

        if (orderRecebida.getType() == OrderType.BUY) {
            orders = orderRepository.findSellOrdersToMatch(orderRecebida.getStockId());
        } else {
            orders =orderRepository.findBuyOrdersToMatch(orderRecebida.getStockId());
        }

        for (Order order : orders) {
            boolean priceMatch;
            if (orderRecebida.getType() == OrderType.BUY){
                priceMatch = order.getUnitPrice().compareTo(orderRecebida.getUnitPrice()) <= 0;
            }else{
                priceMatch = order.getUnitPrice().compareTo(orderRecebida.getUnitPrice()) >= 0;
            } if (!priceMatch) {
                break;
            } else {

            int executedAmount = Math.min(orderRecebida.getRemainingAmount(), order.getRemainingAmount());

            orderRecebida.setRemainingAmount(orderRecebida.getRemainingAmount() - executedAmount);
            order.setRemainingAmount(order.getRemainingAmount() - executedAmount);

            orderRecebida.setExecutedAmount(orderRecebida.getExecutedAmount() + executedAmount);
            order.setExecutedAmount(order.getExecutedAmount() + executedAmount);

            orderRecebida.setStatus(orderRecebida.getRemainingAmount() == 0 ? OrderStatus.EXECUTED : OrderStatus.PARTIAL);
            order.setStatus(order.getRemainingAmount() == 0 ? OrderStatus.EXECUTED : OrderStatus.PARTIAL);

            Trade trade = new Trade(
                        null,
                        orderRecebida.getType() == OrderType.BUY ? orderRecebida.getId() : order.getId(),
                        orderRecebida.getType() == OrderType.SELL ? orderRecebida.getId() : order.getId(),
                        orderRecebida.getStockId(),
                        executedAmount,
                        order.getUnitPrice(),
                        Instant.now()
            );
            tradesRepository.save(trade);
            }

            if (orderRecebida.getRemainingAmount() == 0) {
                break;
            }
        }
            //int remaingAmount;

                /*if(Objects.equals(orderRecebida.getRemainingAmount(), order.getRemainingAmount())){
                    remainingAmount = 0;
                            order.setRemainingAmount(remainingAmount);
                            orderRecebida.setRemainingAmount(remainingAmount);
                            order.setExecutedAmount(order.getRemainingAmount());
                            orderRecebida.setExecutedAmount(orderRecebida.getRemainingAmount());
                } else if (orderRecebida.getRemainingAmount() > order.getRemainingAmount()){
                    remainingAmount = orderRecebida.getRemainingAmount() - order.getRemainingAmount();
                        order.setRemainingAmount(0);
                        orderRecebida.setRemainingAmount(remainingAmount);
                        order.setExecutedAmount(order.getRemainingAmount());
                        orderRecebida.setExecutedAmount(orderRecebida.getTotalAmount() - remainingAmount);

                } else {
                    remainingAmount = order.getRemainingAmount() - orderRecebida.getRemainingAmount();
                    order.setRemainingAmount(remainingAmount);
                    orderRecebida.setRemainingAmount(0);
                    order.setExecutedAmount(orderRecebida.getTotalAmount() - orderRecebida.getRemainingAmount());
                    orderRecebida.setExecutedAmount(orderRecebida.getRemainingAmount());

                }
            } if (orderRecebida.getRemainingAmount() == 0 ) {
                orderRecebida.setStatus(OrderStatus.EXECUTED);
            } if (order.getRemainingAmount() == 0) {
                order.setStatus(OrderStatus.EXECUTED);
            } else {
                orderRecebida.setStatus(OrderStatus.PARTIAL);
                order.setStatus(OrderStatus.PARTIAL);
            }*/
    }
}