package com.example.gakusei.playzz.turf.service;

import com.example.gakusei.playzz.turf.model.Users;

public interface UserService {
     Users registeredUser(Users users);
     Users viewUser(Long userId);
}
