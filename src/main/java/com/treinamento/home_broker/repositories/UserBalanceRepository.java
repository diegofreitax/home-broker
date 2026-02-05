package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.UserBalance;
import com.treinamento.home_broker.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    UserBalance findByUserId(Long user_id);

    Long user(Users user);

    boolean existsByUserId(Long userId);
}
