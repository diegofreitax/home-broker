package com.treinamento.home_broker.DTO;


import com.treinamento.home_broker.domain.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequestDTO {

    private Long userId;
    private Long stockId;
    private OrderType type;
    private BigDecimal unitPrice;
    private Integer totalAmount;

}
