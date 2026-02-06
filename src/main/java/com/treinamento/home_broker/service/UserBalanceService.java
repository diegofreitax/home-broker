package com.treinamento.home_broker.service;

import com.treinamento.home_broker.entities.UserBalance;
import com.treinamento.home_broker.entities.Users;
import com.treinamento.home_broker.repositories.UserBalanceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserBalanceService {

    private final UserBalanceRepository userBalanceRepository;

    @Transactional
    public void createBalance(Long userId){

        boolean exists = userBalanceRepository.existsByUserId(userId);
        if(exists) {
            return;
        }

            UserBalance createdUserBalance = UserBalance.builder()
                    .user(Users.builder().id(userId).build())
                    .availableBalance(BigDecimal.ZERO)
                    .reservedBalance(BigDecimal.ZERO)
                    .build();

            userBalanceRepository.save(createdUserBalance);

    }

    @Transactional
    public void validateBalance(Long userId, BigDecimal requiredAmount){

        if(requiredAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Valor invalido");
        }
        UserBalance userBalance = userBalanceRepository.findByUserId(userId);

        if(userBalance == null){
            throw new IllegalStateException("Saldo não encontrado");
        }
        if(userBalance.getAvailableBalance().compareTo(requiredAmount) < 0){
            throw new IllegalStateException("Saldo insuficiente");
        }
    }

    @Transactional
    public void reserveBalance(Long userId, BigDecimal reservedAmount){

        if(reservedAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Valor invalido");
        }

        UserBalance userBalance = userBalanceRepository.findByUserId(userId);

        if(userBalance == null){
            throw new IllegalStateException("Saldo não encontrado");
        }

        validateBalance(userId, reservedAmount);

        userBalance.setAvailableBalance(userBalance.getAvailableBalance().subtract(reservedAmount));
        userBalance.setReservedBalance(userBalance.getReservedBalance().add(reservedAmount));

        userBalanceRepository.save(userBalance);

    }

    @Transactional
    public void releaseBalance(Long userId, BigDecimal releasedAmount){

        if(releasedAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Valor invalido");
        }

        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if(userBalance == null){
            throw new IllegalStateException("Saldo não encontrado");
        }
        if (userBalance.getReservedBalance().compareTo(releasedAmount) < 0) {
            throw new IllegalStateException("Valor não reservado");
        }
        userBalance.setReservedBalance(userBalance.getReservedBalance().subtract(releasedAmount));
        userBalance.setAvailableBalance(userBalance.getAvailableBalance().add(releasedAmount));

        userBalanceRepository.save(userBalance);
    }

    @Transactional
    public void debitReservedBalance(Long userId, BigDecimal debitedAmount){

        if(debitedAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Valor invalido");
        }

        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if(userBalance == null){
            throw new IllegalStateException("Saldo não encontrado");
        }
        if(userBalance.getReservedBalance().compareTo(debitedAmount) < 0) {
            throw new IllegalStateException("Valor reservado menor que o valor debitado");
        }
        userBalance.setReservedBalance(userBalance.getReservedBalance().subtract(debitedAmount));
        userBalanceRepository.save(userBalance);
    }

    @Transactional
    public void creditAvailableBalance(Long userId, BigDecimal creditedAmount){

        if(creditedAmount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalStateException("Valor invalido");
        }

        UserBalance userBalance = userBalanceRepository.findByUserId(userId);
        if(userBalance == null){
            throw new IllegalStateException("Saldo não encontrado");
        }

        userBalance.setAvailableBalance(userBalance.getAvailableBalance().add(creditedAmount));

        userBalanceRepository.save(userBalance);
    }
}
