package com.yareakh.keyring.service;

import com.yareakh.keyring.dto.UserDTO;

public interface UserRepositoryService {
    UserDTO findByUserName(String userName);
}
