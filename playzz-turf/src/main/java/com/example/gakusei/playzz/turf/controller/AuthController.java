package com.example.gakusei.playzz.turf.controller;

import com.example.gakusei.playzz.turf.jwt.JwtUtil;
import com.example.gakusei.playzz.turf.dto.LoginRequestDto;
import com.example.gakusei.playzz.turf.dto.RegisterRequestDto;
import com.example.gakusei.playzz.turf.implementation.UserServiceImpl;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/playzzturf")
public class AuthController {

     private final UserRepo userRepo;
     private final UserServiceImpl userService;
     private final JwtUtil jwtUtil;

     @Autowired
    public AuthController(UserRepo userRepo, UserServiceImpl userService, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?>registerUser(@RequestBody RegisterRequestDto requestDto){
        if(userRepo.existsByEmail(requestDto.getEmail()) && userRepo.existsByUserName(requestDto.getUsername())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or username already exists");
        }
        Users saveUser = userService.registeredUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saveUser);
    }
    @PostMapping("/login")
    public ResponseEntity<?>loginUser(@RequestBody LoginRequestDto loginRequestDto){
            Users users =userService.loginUser(loginRequestDto);
            String token =jwtUtil.generateToken(
                    new User(users.getUserName(),users.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole().name())))
            );
            return ResponseEntity.status(HttpStatus.OK).body(token);
         }
    }



