package com.example.gakusei.playzz.turf.configure;


import com.example.gakusei.playzz.turf.jwt.JwtAuthFilter;
import com.example.gakusei.playzz.turf.jwt.JwtAuthenticationEntryPoint;

import com.example.gakusei.playzz.turf.model.Role;
import com.example.gakusei.playzz.turf.model.Users;
import com.example.gakusei.playzz.turf.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

@CrossOrigin(origins = "http://localhost:4200")
@Configuration
@EnableWebSecurity
public class AppConfig {


    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthFilter jwtAuthFilter;


    @Autowired
    public AppConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
    @Bean
    CommandLineRunner initAdmin(UserRepo repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByRole(Role.ADMIN)) {
                Users admin = new Users();
                admin.setName("Administrator");
                admin.setUserName("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                admin.setCreatedTimeStamp(LocalDateTime.now());
                repo.save(admin);
                System.out.println(" Default admin created");
            }
        };
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception{
        return configuration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors ->{})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/playzzturf/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/api/playzzturf/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
                http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return  http.build();
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
