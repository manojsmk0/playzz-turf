package com.example.gakusei.playzz.turf.implementation;

import com.example.gakusei.playzz.turf.customexception.UserException;
import com.example.gakusei.playzz.turf.dto.LoginRequestDto;
import com.example.gakusei.playzz.turf.dto.RegisterRequestDto;
import com.example.gakusei.playzz.turf.model.Role;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import com.example.gakusei.playzz.turf.service.EmailService;
import com.example.gakusei.playzz.turf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;
    private EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder,EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService =emailService;
    }

    @Override
    public Users registeredUser(RegisterRequestDto registerRequestDto) {
        Optional<Users>optionalUsers =Optional.ofNullable(userRepo.findByEmail(registerRequestDto.getEmail()));
        if(optionalUsers.isPresent()){
            throw  new UserException("User Name or Email already exists");
        }
        Users users = new Users();
        users.setRole(Role.USER);
        users.setUserName(registerRequestDto.getUsername());
        users.setName(registerRequestDto.getName());
        users.setEmail(registerRequestDto.getEmail());
        String encodedPassword =passwordEncoder.encode(registerRequestDto.getPassword());
        users.setPassword(encodedPassword);
        users.setCreatedTimeStamp(LocalDateTime.now());
            Users savedUser =userRepo.save(users);

        emailService.sendRegisteredEmail(savedUser.getEmail(), savedUser.getName());

        return savedUser;

    }
    @Override
    public Users loginUser(LoginRequestDto loginRequestDto){
        Optional<Users> optionalUsers =userRepo.findByUserName(loginRequestDto.getUserName());
        if(optionalUsers.isEmpty()){
            throw new UserException("Enter a valid UserName");
        }
        if(!passwordEncoder.matches(loginRequestDto.getPassword(), optionalUsers.get().getPassword())){
            throw new UserException("Invalid password");
        }
        return optionalUsers.get();
    }



}
