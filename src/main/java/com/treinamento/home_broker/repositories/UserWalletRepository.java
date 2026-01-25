package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.UserWallet;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    UserWallet findByUserIdAndStockId(Long userId, Long stockId);

}