package com.treinamento.home_broker.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBalanceDepositResponseDTO {

    private BigDecimal availableAmount;
    private BigDecimal reservedAmount;

}
