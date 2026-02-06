package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.UserBalance;
import com.treinamento.home_broker.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    UserBalance findByUserId(Long user_id);

    boolean existsByUserId(Long userId);

    Long user(Users user);
}
