package com.yareakh.keyring.service.impl;

import com.yareakh.keyring.dto.UserDTO;
import com.yareakh.keyring.model.KeyPair;
import com.yareakh.keyring.repository.KeyPairRepository;
import com.yareakh.keyring.service.UserRepositoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {
    KeyPairRepository keyPairRepository;

    public UserRepositoryServiceImpl(KeyPairRepository keyPairRepository) {
        this.keyPairRepository = keyPairRepository;
    }
    @Override
    public UserDTO findByUserName(String userName) {
        List<KeyPair> keyPairList = keyPairRepository.findByName(userName);
        return keyPairList.isEmpty() ? null : new UserDTO(keyPairList.iterator().next());
    }
}
