package com.treinamento.home_broker.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treinamento.home_broker.DTO.UserBalanceDepositRequestDTO;
import com.treinamento.home_broker.entities.UserBalance;
import com.treinamento.home_broker.entities.Users;
import com.treinamento.home_broker.repositories.UserBalanceRepository;
import com.treinamento.home_broker.repositories.UsersRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserBalanceIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDepositAndReturnBalance() throws Exception {

        Users user = Users.builder()
                .email("teste@test.com")
                .password("123456")
                .createdAt(LocalDateTime.now())
                .build();

        Users savedUser = usersRepository.save(user);
        assertThat(savedUser.getId()).isNotNull();

        UserBalance userBalance = UserBalance.builder()
                .user(savedUser)
                .availableBalance(BigDecimal.ZERO)
                .reservedBalance(BigDecimal.ZERO)
                .build();

        userBalanceRepository.save(userBalance);

        UserBalanceDepositRequestDTO depositRequest =
                new UserBalanceDepositRequestDTO(
                        savedUser.getId(),
                        new BigDecimal("100.00")
                );

        mockMvc.perform(post("/balance/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/balance/{userId}", savedUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableAmount").value(100.00))
                .andExpect(jsonPath("$.reservedAmount").value(0));
    }
}