package com.yareakh.keyring.cli;

import com.yareakh.keyring.service.CryptoService;

public class CreatePassword {
    public static final void main(String args[]) {
        String userName = args[0];
        String password = args[1];

        String encryptedPassword = CryptoService.encryptPassword(password);
        System.out.println(String.format("%s:%s", userName, password));
    }
}
