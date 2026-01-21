package com.treinamento.home_broker.service;


import com.treinamento.home_broker.DTO.UserCreateRequestDTO;
import com.treinamento.home_broker.entities.Users;
import com.treinamento.home_broker.repositories.UsersRepository;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final UsersRepository usersRepository;

    public void createUser(UserCreateRequestDTO dto){
        Users users = Users.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
        usersRepository.save(users);
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }
}
