package com.yareakh.keyring.dto;

import com.yareakh.keyring.model.KeyPair;

public class UserDTO {
    private String username;
    private String password;

    public UserDTO() {

    }

    public UserDTO(KeyPair keyPair) {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
