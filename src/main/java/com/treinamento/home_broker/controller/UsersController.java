package com.treinamento.home_broker.controller;

import com.treinamento.home_broker.DTO.UserCreateRequestDTO;
import com.treinamento.home_broker.entities.Users;
import com.treinamento.home_broker.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/create")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequestDTO request) {
        usersService.createUser(request);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity<List<Users>> findAllUser(){
        return ResponseEntity.ok(usersService.findAllUsers());
    }
}
