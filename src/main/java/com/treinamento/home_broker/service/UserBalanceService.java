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

    UserBalanceRepository userBalanceRepository;
    // Criar Saldo do usuario
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
    // Validar disponibilidade financeira
    // Reservar Saldo
    // Liberar Saldo
    // Debitar/Creditar apos matching
}
