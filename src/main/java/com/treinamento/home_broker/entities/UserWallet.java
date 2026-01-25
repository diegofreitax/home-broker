package com.treinamento.home_broker.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_wallets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "stock_id", nullable = false)
    private Long stockId;

    @Column(name = "available_amount", nullable = false)
    private int availableAmount;

    @Column(name = "reserved_amount", nullable = false)
    private int reservedAmount;
}
