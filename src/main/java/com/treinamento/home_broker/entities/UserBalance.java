package com.treinamento.home_broker.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_balances")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true
    )
    private Users user;

    private BigDecimal availableBalance;
    private BigDecimal reservedBalance;
}