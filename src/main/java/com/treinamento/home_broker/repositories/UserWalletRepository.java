package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.UserWallet;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    UserWallet findByUserIdAndStockId(Long userId, Long stockId);

}