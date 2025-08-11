package com.example.gakusei.playzz.turf.dto;

public class RegisterRequestDto {
    private String Username;
    private String name;
    private String email;
    private String password;

    public RegisterRequestDto() {
    }

    public RegisterRequestDto(String username, String name, String email, String password) {
        Username = username;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
