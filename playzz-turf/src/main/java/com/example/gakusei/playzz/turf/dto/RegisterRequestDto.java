package com.example.gakusei.playzz.turf.dto;

public class UserRequestDto {
    private String Username;
    private String email;
    private String password;

    public UserRequestDto() {}

    public UserRequestDto(String username, String email, String password) {
        Username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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
