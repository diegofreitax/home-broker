package com.treinamento.home_broker.controller;


import com.treinamento.home_broker.DTO.UserBalanceDepositRequestDTO;
import com.treinamento.home_broker.DTO.UserBalanceDepositResponseDTO;
import com.treinamento.home_broker.service.UserBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balance")
public class UserBalanceController {

    private final UserBalanceService userBalanceService;

    @PostMapping("/deposit")
    public ResponseEntity<Void> depositAmount(@RequestBody UserBalanceDepositRequestDTO request) {

        userBalanceService.creditAvailableBalance(request.getUserId(), request.getAmount());
        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserBalanceDepositResponseDTO> checkBalance(@PathVariable Long userId){
        UserBalanceDepositResponseDTO response = userBalanceService.checkBalance(userId);
        return ResponseEntity.ok(response);
    }
}
