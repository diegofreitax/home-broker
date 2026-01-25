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

import java.time.Instant;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderMatchingService {


    private final OrderRepository orderRepository;
    private final TradesRepository tradesRepository;
    private final UserWalletRepository userWalletRepository;

    @Transactional
    //metodo de Match de Orders passando Order (entidade) como parametro
    public void matchOrder(Order orderReceived) {
    // Declarando a variavel do tipo Lista de Order com nome orders
        List<Order> orders;
        // Se a orderRecebida passada no parametro do metodo for do tipo BUY
        if (orderReceived.getType() == OrderType.BUY) {
            // a variavel orders declarada vai buscar no repositorio (JPA - DB) pela query definida no find... que busca orders SELL
            orders = orderRepository.findSellOrdersToMatch(orderReceived.getStockId());
        } else { // se for do tipo SELL vai fazer a busca com base na query definida no find.. que busca orders BUY
            orders = orderRepository.findBuyOrdersToMatch(orderReceived.getStockId());
        }
        // Portanto, para utilizar metodos diferentes dos padrões do repositorio, precisamos definir a query do SQL ou os parametros de busca.

        // loop de Variavel do tipo Order passada como order vai iterar na variavel List que foi definida como orders ->
        for (Order order : orders) {
            // declarar variavel boolean de priceMatch para realizar o match de preço
            boolean priceMatch;
            // se o tipo da orderRecebida no metodo for BUY
            if (orderReceived.getType() == OrderType.BUY) {
                // a order de SELL buscada no repositorio precisa ter preço unitario menor ou igual ao da order de BUY para dar match
                priceMatch = order.getUnitPrice().compareTo(orderReceived.getUnitPrice()) <= 0;
            } else {
                // se for do tipo SELL, a order BUY buscada precisa ter preço maior ou igual ao da order SELL para dar match
                priceMatch = order.getUnitPrice().compareTo(orderReceived.getUnitPrice()) >= 0;
            }
            // Se o Match de preço for diferente da regra definida, para o Loop. Pois para o BUY não existe SELL com valor <= e para o SELL nao tem BUY com valor >=
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
                int sellerExecutedAmount = walletSeller.getReservedAmount() - trade.getQuantity();

                if (sellerExecutedAmount <= 0){
                    walletSeller.setReservedAmount(walletSeller.getReservedAmount() - trade.getQuantity());
                    userWalletRepository.save(walletSeller);
                } else {
                    walletSeller.setReservedAmount(sellerExecutedAmount);
                    userWalletRepository.save(walletSeller);
                }

                if (orderReceived.getRemainingAmount() == 0) {
                    break;
                }
            }
        }
    }
}