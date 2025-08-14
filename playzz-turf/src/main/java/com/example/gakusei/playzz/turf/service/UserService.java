package com.example.gakusei.playzz.turf.service;

import com.example.gakusei.playzz.turf.dto.LoginRequestDto;
import com.example.gakusei.playzz.turf.dto.RegisterRequestDto;
import com.example.gakusei.playzz.turf.model.Users;

public interface UserService {
     Users registeredUser(RegisterRequestDto registerRequestDto);
     Users loginUser(LoginRequestDto loginRequestDto);
}
