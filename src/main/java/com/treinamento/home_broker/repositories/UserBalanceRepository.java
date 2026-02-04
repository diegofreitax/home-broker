package com.treinamento.home_broker.repositories;

import com.treinamento.home_broker.entities.UserBalance;
import com.treinamento.home_broker.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

    UserBalanceRepository findByUserId(Long user_id);

    Long user(Users user);

    boolean existsByUserId(Long userId);
}
