package com.example.gakusei.playzz.turf.controller;

import com.example.gakusei.playzz.turf.jwt.JwtUtil;
import com.example.gakusei.playzz.turf.dto.LoginRequestDto;
import com.example.gakusei.playzz.turf.dto.RegisterRequestDto;
import com.example.gakusei.playzz.turf.implementation.UserServiceImpl;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
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

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "login"; // maps to login.html
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDto());
        return "register"; // maps to register.html
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterRequestDto requestDto, Model model) {

        if (userRepo.existsByEmail(requestDto.getEmail()) ||
                userRepo.existsByUserName(requestDto.getUsername())) {

            model.addAttribute("error", "Email or username already exists");
            return "register";
        }


        userService.registeredUser(requestDto);

        return "redirect:/api/public/playzzturf/login";
    }


    @PostMapping("/login")
    public String loginUser(@ModelAttribute LoginRequestDto loginRequestDto,
                            HttpServletResponse response,
                            Model model) {
        try {
            Users users = userService.loginUser(loginRequestDto);

            String token = jwtUtil.generateToken(
                    new User(users.getUserName(),
                            users.getPassword(),
                            List.of(new SimpleGrantedAuthority("ROLE_" + users.getRole().name())))
            );


            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false)
                    .sameSite("lax")
                    .path("/")
                    .maxAge(24 * 60 * 60)
                    .build();
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            return "redirect:/api/playzzturf/booking";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid userName or password");
            return "login";
        }
    }
}


